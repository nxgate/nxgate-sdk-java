package com.nxgate.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nxgate.sdk.model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

/**
 * Cliente principal do SDK NXGATE PIX.
 * <p>
 * Gerencia automaticamente a autenticação OAuth2, assinatura HMAC (quando configurada),
 * e retry com exponential backoff em erros 503.
 *
 * <pre>{@code
 * NXGate nx = NXGate.builder()
 *     .clientId("nxgate_xxx")
 *     .clientSecret("secret")
 *     .hmacSecret("optional")  // opcional
 *     .build();
 *
 * PixGenerateResponse charge = nx.pixGenerate(PixGenerateRequest.builder()
 *     .valor(100.00)
 *     .nomePagador("João da Silva")
 *     .documentoPagador("12345678901")
 *     .build());
 * }</pre>
 */
public class NXGate {

    private static final String SDK_VERSION = "1.0.0";
    private static final String USER_AGENT = "nxgate-sdk-java/" + SDK_VERSION;

    private final NXGateConfig config;
    private final HttpClient httpClient;
    private final Gson gson;
    private final TokenManager tokenManager;
    private final HmacSigner hmacSigner;

    private NXGate(NXGateConfig config) {
        this.config = config;
        this.gson = new GsonBuilder().create();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.getConnectTimeoutMs()))
                .build();
        this.tokenManager = new TokenManager(config, httpClient, gson);
        this.hmacSigner = config.hasHmacSecret()
                ? new HmacSigner(config.getHmacSecret(), config.getClientId())
                : null;
    }

    /**
     * Cria um novo Builder para configuração do SDK.
     *
     * @return NXGateConfig.Builder
     */
    public static NXGateConfig.Builder builder() {
        return new NXGateConfig.Builder();
    }

    /**
     * Cria uma instância NXGate a partir de uma configuração pronta.
     *
     * @param config configuração
     * @return instância NXGate
     */
    public static NXGate create(NXGateConfig config) {
        return new NXGate(config);
    }

    // ============================
    // API Methods
    // ============================

    /**
     * Gera uma cobrança PIX (cash-in) e retorna o QR Code.
     *
     * @param request dados da cobrança
     * @return resposta com QR Code e dados da transação
     * @throws NXGateException se houver erro na requisição
     */
    public PixGenerateResponse pixGenerate(PixGenerateRequest request) throws NXGateException {
        String body = gson.toJson(request);
        String response = doPost("/pix/gerar", body);
        return gson.fromJson(response, PixGenerateResponse.class);
    }

    /**
     * Realiza um saque PIX (cash-out).
     *
     * @param request dados do saque
     * @return resposta com status do saque
     * @throws NXGateException se houver erro na requisição
     */
    public PixWithdrawResponse pixWithdraw(PixWithdrawRequest request) throws NXGateException {
        String body = gson.toJson(request);
        String response = doPost("/pix/sacar", body);
        return gson.fromJson(response, PixWithdrawResponse.class);
    }

    /**
     * Consulta o saldo da conta.
     *
     * @return resposta com saldo, bloqueado e disponível
     * @throws NXGateException se houver erro na requisição
     */
    public BalanceResponse getBalance() throws NXGateException {
        String response = doGet("/v1/balance");
        return gson.fromJson(response, BalanceResponse.class);
    }

    /**
     * Consulta uma transação pelo ID.
     *
     * @param type tipo da transação ("cash-in" ou "cash-out")
     * @param txid ID da transação
     * @return resposta com dados da transação
     * @throws NXGateException se houver erro na requisição
     */
    public TransactionResponse getTransaction(String type, String txid) throws NXGateException {
        if (type == null || type.isEmpty()) {
            throw new NXGateException("O tipo da transação é obrigatório (cash-in ou cash-out)");
        }
        if (txid == null || txid.isEmpty()) {
            throw new NXGateException("O txid da transação é obrigatório");
        }
        String path = "/v1/transactions?type=" + urlEncode(type) + "&txid=" + urlEncode(txid);
        String response = doGet(path);
        return gson.fromJson(response, TransactionResponse.class);
    }

    // ============================
    // HTTP Layer
    // ============================

    private String doPost(String path, String body) throws NXGateException {
        return executeWithRetry("POST", path, body);
    }

    private String doGet(String path) throws NXGateException {
        return executeWithRetry("GET", path, null);
    }

    private String executeWithRetry(String method, String path, String body) throws NXGateException {
        NXGateException lastException = null;
        int maxAttempts = config.getMaxRetries() + 1;

        for (int attempt = 0; attempt < maxAttempts; attempt++) {
            try {
                return execute(method, path, body);
            } catch (NXGateException e) {
                lastException = e;
                if (e.getCode() == 503 && attempt < maxAttempts - 1) {
                    long delayMs = (long) Math.pow(2, attempt) * 1000;
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new NXGateException("Requisição interrompida durante retry", ie);
                    }
                } else {
                    throw e;
                }
            }
        }

        throw lastException;
    }

    private String execute(String method, String path, String body) throws NXGateException {
        try {
            String token = tokenManager.getToken();

            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + path))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("User-Agent", USER_AGENT)
                    .header("Authorization", "Bearer " + token)
                    .timeout(Duration.ofMillis(config.getRequestTimeoutMs()));

            // HMAC signing
            if (hmacSigner != null) {
                Map<String, String> hmacHeaders = hmacSigner.sign(method, path, body);
                for (Map.Entry<String, String> entry : hmacHeaders.entrySet()) {
                    requestBuilder.header(entry.getKey(), entry.getValue());
                }
            }

            // Set method and body
            if ("POST".equalsIgnoreCase(method)) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(
                        body != null ? body : "", StandardCharsets.UTF_8));
            } else {
                requestBuilder.GET();
            }

            HttpResponse<String> response = httpClient.send(
                    requestBuilder.build(),
                    HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            if (statusCode >= 200 && statusCode < 300) {
                return responseBody;
            }

            // Parse error response
            String title = "API Error";
            String description = responseBody;
            String status = "error";

            try {
                JsonObject errorJson = JsonParser.parseString(responseBody).getAsJsonObject();
                if (errorJson.has("title")) {
                    title = errorJson.get("title").getAsString();
                }
                if (errorJson.has("message")) {
                    description = errorJson.get("message").getAsString();
                } else if (errorJson.has("description")) {
                    description = errorJson.get("description").getAsString();
                }
                if (errorJson.has("status")) {
                    status = errorJson.get("status").getAsString();
                }
            } catch (Exception ignored) {
                // Keep raw response body as description
            }

            throw new NXGateException(statusCode, title, description, status);

        } catch (NXGateException e) {
            throw e;
        } catch (Exception e) {
            throw new NXGateException("Erro na requisição: " + e.getMessage(), e);
        }
    }

    private static String urlEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return value;
        }
    }
}
