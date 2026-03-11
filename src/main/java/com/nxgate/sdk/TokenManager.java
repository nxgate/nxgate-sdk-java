package com.nxgate.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

/**
 * Gerencia tokens OAuth2 com cache automático e renovação antes da expiração.
 * <p>
 * O token é cacheado e renovado automaticamente 60 segundos antes de expirar.
 */
public class TokenManager {

    private static final long TOKEN_REFRESH_MARGIN_SECONDS = 60;

    private final NXGateConfig config;
    private final HttpClient httpClient;
    private final Gson gson;

    private String cachedToken;
    private Instant tokenExpiry;
    private final Object lock = new Object();

    /**
     * Cria um novo TokenManager.
     *
     * @param config     configuração do SDK
     * @param httpClient cliente HTTP compartilhado
     * @param gson       instância Gson compartilhada
     */
    public TokenManager(NXGateConfig config, HttpClient httpClient, Gson gson) {
        this.config = config;
        this.httpClient = httpClient;
        this.gson = gson;
    }

    /**
     * Retorna um token válido, renovando automaticamente se necessário.
     *
     * @return token de acesso Bearer
     * @throws NXGateException se não for possível obter o token
     */
    public String getToken() throws NXGateException {
        synchronized (lock) {
            if (cachedToken != null && tokenExpiry != null
                    && Instant.now().plusSeconds(TOKEN_REFRESH_MARGIN_SECONDS).isBefore(tokenExpiry)) {
                return cachedToken;
            }
            return refreshToken();
        }
    }

    /**
     * Força a renovação do token.
     *
     * @throws NXGateException se não for possível obter o token
     */
    public void invalidate() throws NXGateException {
        synchronized (lock) {
            cachedToken = null;
            tokenExpiry = null;
        }
    }

    private String refreshToken() throws NXGateException {
        try {
            JsonObject body = new JsonObject();
            body.addProperty("grant_type", "client_credentials");
            body.addProperty("client_id", config.getClientId());
            body.addProperty("client_secret", config.getClientSecret());

            String jsonBody = gson.toJson(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(config.getBaseUrl() + "/oauth2/token"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofMillis(config.getRequestTimeoutMs()))
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new NXGateException(
                        response.statusCode(),
                        "Authentication Failed",
                        "Falha ao obter token OAuth2: HTTP " + response.statusCode() + " - " + response.body(),
                        "error"
                );
            }

            JsonObject json = gson.fromJson(response.body(), JsonObject.class);

            if (!json.has("access_token")) {
                throw new NXGateException(
                        response.statusCode(),
                        "Invalid Token Response",
                        "Resposta de token não contém access_token: " + response.body(),
                        "error"
                );
            }

            cachedToken = json.get("access_token").getAsString();
            long expiresIn = json.has("expires_in") ? json.get("expires_in").getAsLong() : 3600;
            tokenExpiry = Instant.now().plusSeconds(expiresIn);

            return cachedToken;

        } catch (NXGateException e) {
            throw e;
        } catch (Exception e) {
            throw new NXGateException("Erro ao obter token: " + e.getMessage(), e);
        }
    }
}
