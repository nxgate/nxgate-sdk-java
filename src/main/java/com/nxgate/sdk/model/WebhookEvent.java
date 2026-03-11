package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Classe base para eventos de webhook NXGATE.
 * Use {@code instanceof} para verificar se é {@link CashInWebhookEvent} ou {@link CashOutWebhookEvent}.
 */
public class WebhookEvent {

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("worked")
    private Boolean worked;

    public WebhookEvent() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getWorked() {
        return worked;
    }

    public void setWorked(Boolean worked) {
        this.worked = worked;
    }

    /**
     * Verifica se o evento é de cash-in.
     */
    public boolean isCashIn() {
        return this instanceof CashInWebhookEvent;
    }

    /**
     * Verifica se o evento é de cash-out.
     */
    public boolean isCashOut() {
        return this instanceof CashOutWebhookEvent;
    }

    /**
     * Cast seguro para CashInWebhookEvent.
     *
     * @return CashInWebhookEvent ou null se não for cash-in
     */
    public CashInWebhookEvent asCashIn() {
        return this instanceof CashInWebhookEvent ? (CashInWebhookEvent) this : null;
    }

    /**
     * Cast seguro para CashOutWebhookEvent.
     *
     * @return CashOutWebhookEvent ou null se não for cash-out
     */
    public CashOutWebhookEvent asCashOut() {
        return this instanceof CashOutWebhookEvent ? (CashOutWebhookEvent) this : null;
    }

    @Override
    public String toString() {
        return "WebhookEvent{type='" + type + "', status='" + status + "'}";
    }
}
