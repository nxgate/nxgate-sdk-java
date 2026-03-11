package com.nxgate.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nxgate.sdk.model.CashInWebhookEvent;
import com.nxgate.sdk.model.CashOutWebhookEvent;
import com.nxgate.sdk.model.WebhookEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser de eventos webhook da NXGATE.
 * <p>
 * Detecta automaticamente se o evento é cash-in ou cash-out com base no campo {@code type}
 * e retorna a subclasse apropriada.
 *
 * <pre>{@code
 * WebhookEvent event = NXGateWebhook.parse(jsonString);
 * if (event.isCashIn()) {
 *     CashInWebhookEvent cashIn = event.asCashIn();
 *     System.out.println(cashIn.getData().getTxId());
 * } else if (event.isCashOut()) {
 *     CashOutWebhookEvent cashOut = event.asCashOut();
 *     System.out.println(cashOut.getIdTransaction());
 * }
 * }</pre>
 */
public class NXGateWebhook {

    private static final Gson GSON = new Gson();

    private static final Set<String> CASH_IN_TYPES = new HashSet<>(Arrays.asList(
            "QR_CODE_COPY_AND_PASTE_PAID",
            "QR_CODE_COPY_AND_PASTE_REFUNDED"
    ));

    private static final Set<String> CASH_OUT_TYPES = new HashSet<>(Arrays.asList(
            "PIX_CASHOUT_SUCCESS",
            "PIX_CASHOUT_ERROR",
            "PIX_CASHOUT_REFUNDED"
    ));

    private NXGateWebhook() {
        // Utility class
    }

    /**
     * Faz o parse de uma string JSON de webhook e retorna o evento tipado.
     *
     * @param json string JSON do webhook
     * @return WebhookEvent (CashInWebhookEvent ou CashOutWebhookEvent)
     * @throws NXGateException se o JSON for inválido ou o tipo não for reconhecido
     */
    public static WebhookEvent parse(String json) throws NXGateException {
        if (json == null || json.isEmpty()) {
            throw new NXGateException("JSON do webhook está vazio ou nulo");
        }

        try {
            JsonElement element = JsonParser.parseString(json);
            if (!element.isJsonObject()) {
                throw new NXGateException("JSON do webhook deve ser um objeto");
            }

            JsonObject obj = element.getAsJsonObject();

            if (!obj.has("type")) {
                throw new NXGateException("JSON do webhook não contém o campo 'type'");
            }

            String type = obj.get("type").getAsString();

            if (CASH_IN_TYPES.contains(type)) {
                return GSON.fromJson(json, CashInWebhookEvent.class);
            } else if (CASH_OUT_TYPES.contains(type)) {
                return GSON.fromJson(json, CashOutWebhookEvent.class);
            } else {
                // Tipo desconhecido - retorna evento genérico
                return GSON.fromJson(json, WebhookEvent.class);
            }

        } catch (NXGateException e) {
            throw e;
        } catch (Exception e) {
            throw new NXGateException("Erro ao fazer parse do webhook: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica se um tipo de evento é cash-in.
     *
     * @param type tipo do evento
     * @return true se for cash-in
     */
    public static boolean isCashInType(String type) {
        return type != null && CASH_IN_TYPES.contains(type);
    }

    /**
     * Verifica se um tipo de evento é cash-out.
     *
     * @param type tipo do evento
     * @return true se for cash-out
     */
    public static boolean isCashOutType(String type) {
        return type != null && CASH_OUT_TYPES.contains(type);
    }
}
