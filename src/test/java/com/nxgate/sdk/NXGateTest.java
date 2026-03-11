package com.nxgate.sdk;

import com.nxgate.sdk.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o cliente NXGate.
 */
class NXGateTest {

    @Test
    @DisplayName("Builder deve criar instância com configuração mínima")
    void testBuilderMinimalConfig() {
        NXGateConfig config = NXGate.builder()
                .clientId("test_client_id")
                .clientSecret("test_secret")
                .build();

        assertNotNull(config);
        assertEquals("test_client_id", config.getClientId());
        assertEquals("test_secret", config.getClientSecret());
        assertNull(config.getHmacSecret());
        assertFalse(config.hasHmacSecret());
        assertEquals("https://api.nxgate.com.br", config.getBaseUrl());
    }

    @Test
    @DisplayName("Builder deve criar instância com todas as opções")
    void testBuilderFullConfig() {
        NXGateConfig config = NXGate.builder()
                .clientId("test_client_id")
                .clientSecret("test_secret")
                .hmacSecret("hmac_secret_key")
                .baseUrl("https://sandbox.nxgate.com.br")
                .maxRetries(3)
                .connectTimeoutMs(5000)
                .requestTimeoutMs(15000)
                .build();

        assertNotNull(config);
        assertEquals("test_client_id", config.getClientId());
        assertEquals("test_secret", config.getClientSecret());
        assertEquals("hmac_secret_key", config.getHmacSecret());
        assertTrue(config.hasHmacSecret());
        assertEquals("https://sandbox.nxgate.com.br", config.getBaseUrl());
        assertEquals(3, config.getMaxRetries());
        assertEquals(5000, config.getConnectTimeoutMs());
        assertEquals(15000, config.getRequestTimeoutMs());
    }

    @Test
    @DisplayName("Builder deve lançar exceção sem clientId")
    void testBuilderMissingClientId() {
        assertThrows(IllegalArgumentException.class, () ->
                NXGate.builder()
                        .clientSecret("test_secret")
                        .build()
        );
    }

    @Test
    @DisplayName("Builder deve lançar exceção sem clientSecret")
    void testBuilderMissingClientSecret() {
        assertThrows(IllegalArgumentException.class, () ->
                NXGate.builder()
                        .clientId("test_client_id")
                        .build()
        );
    }

    @Test
    @DisplayName("Builder deve lançar exceção com clientId vazio")
    void testBuilderEmptyClientId() {
        assertThrows(IllegalArgumentException.class, () ->
                NXGate.builder()
                        .clientId("")
                        .clientSecret("test_secret")
                        .build()
        );
    }

    @Test
    @DisplayName("NXGate.create deve funcionar com config válida")
    void testCreateWithConfig() {
        NXGateConfig config = NXGate.builder()
                .clientId("test_id")
                .clientSecret("test_secret")
                .build();

        NXGate client = NXGate.create(config);
        assertNotNull(client);
    }

    // ==========================================
    // PixGenerateRequest Tests
    // ==========================================

    @Test
    @DisplayName("PixGenerateRequest builder deve criar request válido")
    void testPixGenerateRequestBuilder() {
        PixGenerateRequest request = PixGenerateRequest.builder()
                .valor(100.50)
                .nomePagador("João da Silva")
                .documentoPagador("12345678901")
                .emailPagador("joao@email.com")
                .celular("11999998888")
                .descricao("Pagamento teste")
                .webhook("https://meusite.com/webhook")
                .magicId("magic123")
                .forcarPagador(true)
                .build();

        assertEquals(100.50, request.getValor());
        assertEquals("João da Silva", request.getNomePagador());
        assertEquals("12345678901", request.getDocumentoPagador());
        assertEquals("joao@email.com", request.getEmailPagador());
        assertEquals("11999998888", request.getCelular());
        assertEquals("Pagamento teste", request.getDescricao());
        assertEquals("https://meusite.com/webhook", request.getWebhook());
        assertEquals("magic123", request.getMagicId());
        assertTrue(request.getForcarPagador());
    }

    @Test
    @DisplayName("PixGenerateRequest builder deve aceitar split_users")
    void testPixGenerateRequestWithSplit() {
        PixGenerateRequest request = PixGenerateRequest.builder()
                .valor(200.00)
                .nomePagador("Maria")
                .documentoPagador("98765432100")
                .splitUsers(Arrays.asList(
                        new PixGenerateRequest.SplitUser("user1", 60.0),
                        new PixGenerateRequest.SplitUser("user2", 40.0)
                ))
                .build();

        assertNotNull(request.getSplitUsers());
        assertEquals(2, request.getSplitUsers().size());
        assertEquals("user1", request.getSplitUsers().get(0).getUsername());
        assertEquals(60.0, request.getSplitUsers().get(0).getPercentage());
    }

    @Test
    @DisplayName("PixGenerateRequest deve falhar sem valor")
    void testPixGenerateRequestMissingValor() {
        assertThrows(IllegalArgumentException.class, () ->
                PixGenerateRequest.builder()
                        .nomePagador("João")
                        .documentoPagador("12345678901")
                        .build()
        );
    }

    @Test
    @DisplayName("PixGenerateRequest deve falhar com valor zero")
    void testPixGenerateRequestZeroValor() {
        assertThrows(IllegalArgumentException.class, () ->
                PixGenerateRequest.builder()
                        .valor(0)
                        .nomePagador("João")
                        .documentoPagador("12345678901")
                        .build()
        );
    }

    @Test
    @DisplayName("PixGenerateRequest deve falhar sem nomePagador")
    void testPixGenerateRequestMissingNome() {
        assertThrows(IllegalArgumentException.class, () ->
                PixGenerateRequest.builder()
                        .valor(100.0)
                        .documentoPagador("12345678901")
                        .build()
        );
    }

    @Test
    @DisplayName("PixGenerateRequest deve falhar sem documentoPagador")
    void testPixGenerateRequestMissingDocumento() {
        assertThrows(IllegalArgumentException.class, () ->
                PixGenerateRequest.builder()
                        .valor(100.0)
                        .nomePagador("João")
                        .build()
        );
    }

    // ==========================================
    // PixWithdrawRequest Tests
    // ==========================================

    @Test
    @DisplayName("PixWithdrawRequest builder deve criar request válido")
    void testPixWithdrawRequestBuilder() {
        PixWithdrawRequest request = PixWithdrawRequest.builder()
                .valor(50.0)
                .chavePix("joao@email.com")
                .tipoChave(PixKeyType.EMAIL)
                .documento("12345678901")
                .webhook("https://meusite.com/webhook")
                .build();

        assertEquals(50.0, request.getValor());
        assertEquals("joao@email.com", request.getChavePix());
        assertEquals("EMAIL", request.getTipoChave());
        assertEquals("12345678901", request.getDocumento());
    }

    @Test
    @DisplayName("PixWithdrawRequest deve falhar sem chavePix")
    void testPixWithdrawRequestMissingChave() {
        assertThrows(IllegalArgumentException.class, () ->
                PixWithdrawRequest.builder()
                        .valor(50.0)
                        .tipoChave(PixKeyType.CPF)
                        .build()
        );
    }

    @Test
    @DisplayName("PixWithdrawRequest deve falhar sem tipoChave")
    void testPixWithdrawRequestMissingTipo() {
        assertThrows(IllegalArgumentException.class, () ->
                PixWithdrawRequest.builder()
                        .valor(50.0)
                        .chavePix("12345678901")
                        .build()
        );
    }

    // ==========================================
    // PixKeyType Tests
    // ==========================================

    @Test
    @DisplayName("PixKeyType.fromValue deve converter valores válidos")
    void testPixKeyTypeFromValue() {
        assertEquals(PixKeyType.CPF, PixKeyType.fromValue("CPF"));
        assertEquals(PixKeyType.CNPJ, PixKeyType.fromValue("CNPJ"));
        assertEquals(PixKeyType.PHONE, PixKeyType.fromValue("PHONE"));
        assertEquals(PixKeyType.EMAIL, PixKeyType.fromValue("EMAIL"));
        assertEquals(PixKeyType.RANDOM, PixKeyType.fromValue("RANDOM"));
    }

    @Test
    @DisplayName("PixKeyType.fromValue deve aceitar case insensitive")
    void testPixKeyTypeFromValueCaseInsensitive() {
        assertEquals(PixKeyType.CPF, PixKeyType.fromValue("cpf"));
        assertEquals(PixKeyType.EMAIL, PixKeyType.fromValue("email"));
    }

    @Test
    @DisplayName("PixKeyType.fromValue deve falhar com valor inválido")
    void testPixKeyTypeFromValueInvalid() {
        assertThrows(IllegalArgumentException.class, () -> PixKeyType.fromValue("INVALID"));
    }

    @Test
    @DisplayName("PixKeyType.fromValue deve falhar com null")
    void testPixKeyTypeFromValueNull() {
        assertThrows(IllegalArgumentException.class, () -> PixKeyType.fromValue(null));
    }
}
