package com.nxgate.sdk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para NXGateException.
 */
class NXGateExceptionTest {

    @Test
    @DisplayName("Construtor com código, título, descrição e status")
    void testFullConstructor() {
        NXGateException ex = new NXGateException(401, "Unauthorized", "Token inválido", "error");

        assertEquals(401, ex.getCode());
        assertEquals("Unauthorized", ex.getTitle());
        assertEquals("Token inválido", ex.getDescription());
        assertEquals("error", ex.getStatus());
        assertTrue(ex.getMessage().contains("401"));
        assertTrue(ex.getMessage().contains("Unauthorized"));
        assertTrue(ex.getMessage().contains("Token inválido"));
    }

    @Test
    @DisplayName("Construtor com mensagem simples")
    void testSimpleMessageConstructor() {
        NXGateException ex = new NXGateException("Erro de conexão");

        assertEquals(0, ex.getCode());
        assertEquals("SDK Error", ex.getTitle());
        assertEquals("Erro de conexão", ex.getDescription());
        assertEquals("error", ex.getStatus());
        assertEquals("Erro de conexão", ex.getMessage());
    }

    @Test
    @DisplayName("Construtor com mensagem e causa")
    void testMessageAndCauseConstructor() {
        RuntimeException cause = new RuntimeException("Connection refused");
        NXGateException ex = new NXGateException("Falha na conexão", cause);

        assertEquals(0, ex.getCode());
        assertEquals("SDK Error", ex.getTitle());
        assertEquals("Falha na conexão", ex.getDescription());
        assertEquals(cause, ex.getCause());
    }

    @Test
    @DisplayName("toString deve retornar a mensagem formatada")
    void testToString() {
        NXGateException ex = new NXGateException(500, "Server Error", "Internal error", "error");
        String str = ex.toString();

        assertNotNull(str);
        assertTrue(str.contains("500"));
        assertTrue(str.contains("Server Error"));
        assertTrue(str.contains("Internal error"));
    }

    @Test
    @DisplayName("Construtor com campos nulos deve funcionar")
    void testNullFields() {
        NXGateException ex = new NXGateException(404, null, null, null);

        assertEquals(404, ex.getCode());
        assertNull(ex.getTitle());
        assertNull(ex.getDescription());
        assertNull(ex.getStatus());
        assertTrue(ex.getMessage().contains("404"));
    }

    @Test
    @DisplayName("NXGateException deve ser instância de Exception")
    void testIsException() {
        NXGateException ex = new NXGateException("test");
        assertInstanceOf(Exception.class, ex);
    }

    @Test
    @DisplayName("Códigos HTTP comuns devem ser representáveis")
    void testCommonHttpCodes() {
        NXGateException ex400 = new NXGateException(400, "Bad Request", "Campos inválidos", "error");
        NXGateException ex401 = new NXGateException(401, "Unauthorized", "Token expirado", "error");
        NXGateException ex403 = new NXGateException(403, "Forbidden", "Sem permissão", "error");
        NXGateException ex422 = new NXGateException(422, "Unprocessable Entity", "Validação falhou", "error");
        NXGateException ex500 = new NXGateException(500, "Internal Server Error", "Erro interno", "error");
        NXGateException ex503 = new NXGateException(503, "Service Unavailable", "Serviço indisponível", "error");

        assertEquals(400, ex400.getCode());
        assertEquals(401, ex401.getCode());
        assertEquals(403, ex403.getCode());
        assertEquals(422, ex422.getCode());
        assertEquals(500, ex500.getCode());
        assertEquals(503, ex503.getCode());
    }
}
