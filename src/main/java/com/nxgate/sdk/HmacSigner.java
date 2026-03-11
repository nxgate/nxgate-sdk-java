package com.nxgate.sdk;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * Gera assinaturas HMAC-SHA256 para autenticação de requisições.
 * <p>
 * O payload assinado segue o formato:
 * <pre>
 * METHOD\nPATH\nTIMESTAMP\nNONCE\nBODY
 * </pre>
 */
public class HmacSigner {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final DateTimeFormatter ISO_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .withZone(ZoneOffset.UTC);

    private final String hmacSecret;
    private final String clientId;

    /**
     * Cria um novo HmacSigner.
     *
     * @param hmacSecret chave secreta HMAC
     * @param clientId   ID do cliente
     */
    public HmacSigner(String hmacSecret, String clientId) {
        this.hmacSecret = hmacSecret;
        this.clientId = clientId;
    }

    /**
     * Gera os headers HMAC para uma requisição.
     *
     * @param method método HTTP (GET, POST, etc.)
     * @param path   caminho da requisição (ex: /pix/gerar)
     * @param body   corpo da requisição (vazio para GET)
     * @return mapa com os headers HMAC necessários
     * @throws NXGateException se houver erro na geração da assinatura
     */
    public Map<String, String> sign(String method, String path, String body) throws NXGateException {
        String timestamp = ISO_FORMATTER.format(Instant.now());
        String nonce = UUID.randomUUID().toString();
        String signature = computeSignature(method, path, timestamp, nonce, body);

        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("X-Client-ID", clientId);
        headers.put("X-HMAC-Signature", signature);
        headers.put("X-HMAC-Timestamp", timestamp);
        headers.put("X-HMAC-Nonce", nonce);
        return headers;
    }

    /**
     * Computa a assinatura HMAC-SHA256 e retorna em Base64.
     *
     * @param method    método HTTP
     * @param path      caminho da requisição
     * @param timestamp timestamp ISO 8601
     * @param nonce     nonce único
     * @param body      corpo da requisição
     * @return assinatura em Base64
     * @throws NXGateException se houver erro no cálculo
     */
    String computeSignature(String method, String path, String timestamp, String nonce, String body)
            throws NXGateException {
        try {
            String payload = method.toUpperCase() + "\n"
                    + path + "\n"
                    + timestamp + "\n"
                    + nonce + "\n"
                    + (body != null ? body : "");

            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    hmacSecret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM);
            mac.init(keySpec);

            byte[] rawHmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new NXGateException("Erro ao gerar assinatura HMAC: " + e.getMessage(), e);
        }
    }
}
