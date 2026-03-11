package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Evento de webhook para transações cash-out (saques PIX).
 * <p>
 * Tipos possíveis:
 * <ul>
 *   <li>{@code PIX_CASHOUT_SUCCESS} - saque realizado com sucesso</li>
 *   <li>{@code PIX_CASHOUT_ERROR} - erro no saque</li>
 *   <li>{@code PIX_CASHOUT_REFUNDED} - saque estornado</li>
 * </ul>
 */
public class CashOutWebhookEvent extends WebhookEvent {

    @SerializedName("idTransaction")
    private String idTransaction;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("key")
    private String key;

    @SerializedName("end_to_end")
    private String endToEnd;

    @SerializedName("payment_date")
    private String paymentDate;

    @SerializedName("magic_id")
    private String magicId;

    @SerializedName("fee")
    private Double fee;

    @SerializedName("error")
    private String error;

    public CashOutWebhookEvent() {
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEndToEnd() {
        return endToEnd;
    }

    public void setEndToEnd(String endToEnd) {
        this.endToEnd = endToEnd;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getMagicId() {
        return magicId;
    }

    public void setMagicId(String magicId) {
        this.magicId = magicId;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CashOutWebhookEvent{" +
                "type='" + getType() + '\'' +
                ", idTransaction='" + idTransaction + '\'' +
                ", amount=" + amount +
                ", key='" + key + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
