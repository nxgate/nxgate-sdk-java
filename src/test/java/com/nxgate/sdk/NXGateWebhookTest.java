package com.nxgate.sdk;

import com.nxgate.sdk.model.CashInWebhookEvent;
import com.nxgate.sdk.model.CashOutWebhookEvent;
import com.nxgate.sdk.model.WebhookEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para NXGateWebhook.
 */
class NXGateWebhookTest {

    @Test
    @DisplayName("parse deve identificar evento cash-in PAID")
    void testParseCashInPaid() throws NXGateException {
        String json = "{"
                + "\"type\":\"QR_CODE_COPY_AND_PASTE_PAID\","
                + "\"data\":{"
                + "\"amount\":100.50,"
                + "\"status\":\"PAID\","
                + "\"worked\":true,"
                + "\"tag\":\"tag123\","
                + "\"tx_id\":\"tx_abc123\","
                + "\"end_to_end\":\"e2e_xyz\","
                + "\"payment_date\":\"2024-01-15T10:30:00Z\","
                + "\"debtor_name\":\"João da Silva\","
                + "\"debtor_document\":\"12345678901\","
                + "\"type_document\":\"CPF\","
                + "\"magic_id\":\"magic_001\","
                + "\"fee\":1.50"
                + "}"
                + "}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertNotNull(event);
        assertTrue(event.isCashIn());
        assertFalse(event.isCashOut());
        assertEquals("QR_CODE_COPY_AND_PASTE_PAID", event.getType());

        CashInWebhookEvent cashIn = event.asCashIn();
        assertNotNull(cashIn);
        assertNotNull(cashIn.getData());
        assertEquals(100.50, cashIn.getData().getAmount());
        assertEquals("PAID", cashIn.getData().getStatus());
        assertTrue(cashIn.getData().getWorked());
        assertEquals("tx_abc123", cashIn.getData().getTxId());
        assertEquals("e2e_xyz", cashIn.getData().getEndToEnd());
        assertEquals("João da Silva", cashIn.getData().getDebtorName());
        assertEquals("12345678901", cashIn.getData().getDebtorDocument());
        assertEquals("magic_001", cashIn.getData().getMagicId());
        assertEquals(1.50, cashIn.getData().getFee());
    }

    @Test
    @DisplayName("parse deve identificar evento cash-in REFUNDED")
    void testParseCashInRefunded() throws NXGateException {
        String json = "{"
                + "\"type\":\"QR_CODE_COPY_AND_PASTE_REFUNDED\","
                + "\"data\":{"
                + "\"amount\":50.00,"
                + "\"status\":\"REFUNDED\","
                + "\"tx_id\":\"tx_refund\""
                + "}"
                + "}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertTrue(event.isCashIn());
        assertEquals("QR_CODE_COPY_AND_PASTE_REFUNDED", event.getType());
        CashInWebhookEvent cashIn = event.asCashIn();
        assertEquals(50.00, cashIn.getData().getAmount());
        assertEquals("REFUNDED", cashIn.getData().getStatus());
    }

    @Test
    @DisplayName("parse deve identificar evento cash-out SUCCESS")
    void testParseCashOutSuccess() throws NXGateException {
        String json = "{"
                + "\"type\":\"PIX_CASHOUT_SUCCESS\","
                + "\"worked\":true,"
                + "\"status\":\"SUCCESS\","
                + "\"idTransaction\":\"txn_123\","
                + "\"amount\":200.00,"
                + "\"key\":\"joao@email.com\","
                + "\"end_to_end\":\"e2e_456\","
                + "\"payment_date\":\"2024-01-15T11:00:00Z\","
                + "\"magic_id\":\"magic_002\","
                + "\"fee\":2.00"
                + "}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertNotNull(event);
        assertFalse(event.isCashIn());
        assertTrue(event.isCashOut());
        assertEquals("PIX_CASHOUT_SUCCESS", event.getType());

        CashOutWebhookEvent cashOut = event.asCashOut();
        assertNotNull(cashOut);
        assertEquals("txn_123", cashOut.getIdTransaction());
        assertEquals(200.00, cashOut.getAmount());
        assertEquals("joao@email.com", cashOut.getKey());
        assertEquals("e2e_456", cashOut.getEndToEnd());
        assertEquals("magic_002", cashOut.getMagicId());
        assertEquals(2.00, cashOut.getFee());
        assertTrue(cashOut.getWorked());
        assertNull(cashOut.getError());
    }

    @Test
    @DisplayName("parse deve identificar evento cash-out ERROR")
    void testParseCashOutError() throws NXGateException {
        String json = "{"
                + "\"type\":\"PIX_CASHOUT_ERROR\","
                + "\"worked\":false,"
                + "\"status\":\"ERROR\","
                + "\"idTransaction\":\"txn_err\","
                + "\"amount\":100.00,"
                + "\"key\":\"12345678901\","
                + "\"error\":\"Chave PIX inválida\""
                + "}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertTrue(event.isCashOut());
        CashOutWebhookEvent cashOut = event.asCashOut();
        assertEquals("PIX_CASHOUT_ERROR", cashOut.getType());
        assertFalse(cashOut.getWorked());
        assertEquals("Chave PIX inválida", cashOut.getError());
    }

    @Test
    @DisplayName("parse deve identificar evento cash-out REFUNDED")
    void testParseCashOutRefunded() throws NXGateException {
        String json = "{"
                + "\"type\":\"PIX_CASHOUT_REFUNDED\","
                + "\"worked\":true,"
                + "\"status\":\"REFUNDED\","
                + "\"idTransaction\":\"txn_ref\","
                + "\"amount\":75.00,"
                + "\"key\":\"random-key-123\""
                + "}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertTrue(event.isCashOut());
        assertEquals("PIX_CASHOUT_REFUNDED", event.getType());
    }

    @Test
    @DisplayName("parse deve lançar exceção para JSON nulo")
    void testParseNullJson() {
        assertThrows(NXGateException.class, () -> NXGateWebhook.parse(null));
    }

    @Test
    @DisplayName("parse deve lançar exceção para JSON vazio")
    void testParseEmptyJson() {
        assertThrows(NXGateException.class, () -> NXGateWebhook.parse(""));
    }

    @Test
    @DisplayName("parse deve lançar exceção para JSON inválido")
    void testParseInvalidJson() {
        assertThrows(NXGateException.class, () -> NXGateWebhook.parse("not json"));
    }

    @Test
    @DisplayName("parse deve lançar exceção para JSON sem campo type")
    void testParseMissingType() {
        assertThrows(NXGateException.class, () ->
                NXGateWebhook.parse("{\"status\":\"OK\"}")
        );
    }

    @Test
    @DisplayName("parse deve retornar WebhookEvent genérico para tipo desconhecido")
    void testParseUnknownType() throws NXGateException {
        String json = "{\"type\":\"UNKNOWN_TYPE\",\"status\":\"OK\"}";

        WebhookEvent event = NXGateWebhook.parse(json);

        assertNotNull(event);
        assertFalse(event.isCashIn());
        assertFalse(event.isCashOut());
        assertEquals("UNKNOWN_TYPE", event.getType());
        assertNull(event.asCashIn());
        assertNull(event.asCashOut());
    }

    @Test
    @DisplayName("isCashInType deve retornar true para tipos cash-in")
    void testIsCashInType() {
        assertTrue(NXGateWebhook.isCashInType("QR_CODE_COPY_AND_PASTE_PAID"));
        assertTrue(NXGateWebhook.isCashInType("QR_CODE_COPY_AND_PASTE_REFUNDED"));
        assertFalse(NXGateWebhook.isCashInType("PIX_CASHOUT_SUCCESS"));
        assertFalse(NXGateWebhook.isCashInType(null));
    }

    @Test
    @DisplayName("isCashOutType deve retornar true para tipos cash-out")
    void testIsCashOutType() {
        assertTrue(NXGateWebhook.isCashOutType("PIX_CASHOUT_SUCCESS"));
        assertTrue(NXGateWebhook.isCashOutType("PIX_CASHOUT_ERROR"));
        assertTrue(NXGateWebhook.isCashOutType("PIX_CASHOUT_REFUNDED"));
        assertFalse(NXGateWebhook.isCashOutType("QR_CODE_COPY_AND_PASTE_PAID"));
        assertFalse(NXGateWebhook.isCashOutType(null));
    }
}
