package com.nxgate.sdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Base64;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para HmacSigner.
 */
class HmacSignerTest {

    private HmacSigner signer;

    @BeforeEach
    void setUp() {
        signer = new HmacSigner("test_hmac_secret", "test_client_id");
    }

    @Test
    @DisplayName("sign deve retornar todos os headers HMAC necessários")
    void testSignReturnsAllHeaders() throws NXGateException {
        Map<String, String> headers = signer.sign("POST", "/pix/gerar", "{\"valor\":100}");

        assertNotNull(headers);
        assertTrue(headers.containsKey("X-Client-ID"));
        assertTrue(headers.containsKey("X-HMAC-Signature"));
        assertTrue(headers.containsKey("X-HMAC-Timestamp"));
        assertTrue(headers.containsKey("X-HMAC-Nonce"));
    }

    @Test
    @DisplayName("sign deve retornar X-Client-ID correto")
    void testSignClientId() throws NXGateException {
        Map<String, String> headers = signer.sign("GET", "/v1/balance", null);
        assertEquals("test_client_id", headers.get("X-Client-ID"));
    }

    @Test
    @DisplayName("sign deve retornar assinatura Base64 válida")
    void testSignatureIsValidBase64() throws NXGateException {
        Map<String, String> headers = signer.sign("POST", "/pix/gerar", "{\"valor\":100}");

        String signature = headers.get("X-HMAC-Signature");
        assertNotNull(signature);
        assertFalse(signature.isEmpty());

        // Deve ser Base64 válido
        assertDoesNotThrow(() -> Base64.getDecoder().decode(signature));
    }

    @Test
    @DisplayName("sign deve retornar timestamp ISO 8601")
    void testTimestampFormat() throws NXGateException {
        Map<String, String> headers = signer.sign("POST", "/pix/gerar", "{}");

        String timestamp = headers.get("X-HMAC-Timestamp");
        assertNotNull(timestamp);
        // ISO 8601 format: yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
        assertTrue(timestamp.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z"),
                "Timestamp deve estar no formato ISO 8601: " + timestamp);
    }

    @Test
    @DisplayName("sign deve retornar nonce único por chamada")
    void testNonceIsUnique() throws NXGateException {
        Map<String, String> headers1 = signer.sign("POST", "/pix/gerar", "{}");
        Map<String, String> headers2 = signer.sign("POST", "/pix/gerar", "{}");

        assertNotEquals(headers1.get("X-HMAC-Nonce"), headers2.get("X-HMAC-Nonce"),
                "Nonce deve ser único por requisição");
    }

    @Test
    @DisplayName("computeSignature deve gerar assinatura determinística")
    void testComputeSignatureDeterministic() throws NXGateException {
        String sig1 = signer.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "fixed-nonce", "{\"valor\":100}");
        String sig2 = signer.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "fixed-nonce", "{\"valor\":100}");

        assertEquals(sig1, sig2, "Mesmos inputs devem gerar a mesma assinatura");
    }

    @Test
    @DisplayName("computeSignature deve diferir com inputs diferentes")
    void testComputeSignatureDifferentInputs() throws NXGateException {
        String sig1 = signer.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "nonce1", "{\"valor\":100}");
        String sig2 = signer.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "nonce2", "{\"valor\":100}");

        assertNotEquals(sig1, sig2, "Inputs diferentes devem gerar assinaturas diferentes");
    }

    @Test
    @DisplayName("sign deve funcionar com body null para GET")
    void testSignWithNullBody() throws NXGateException {
        Map<String, String> headers = signer.sign("GET", "/v1/balance", null);
        assertNotNull(headers.get("X-HMAC-Signature"));
    }

    @Test
    @DisplayName("sign deve funcionar com body vazio")
    void testSignWithEmptyBody() throws NXGateException {
        Map<String, String> headers = signer.sign("GET", "/v1/balance", "");
        assertNotNull(headers.get("X-HMAC-Signature"));
    }

    @Test
    @DisplayName("computeSignature com secrets diferentes deve gerar assinaturas diferentes")
    void testDifferentSecrets() throws NXGateException {
        HmacSigner signer2 = new HmacSigner("different_secret", "test_client_id");

        String sig1 = signer.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "nonce", "{}");
        String sig2 = signer2.computeSignature("POST", "/pix/gerar",
                "2024-01-01T00:00:00.000Z", "nonce", "{}");

        assertNotEquals(sig1, sig2, "Secrets diferentes devem gerar assinaturas diferentes");
    }
}
