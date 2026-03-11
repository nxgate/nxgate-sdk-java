package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Resposta da geração de cobrança PIX (cash-in).
 */
public class PixGenerateResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("paymentCode")
    private String paymentCode;

    @SerializedName("idTransaction")
    private String idTransaction;

    @SerializedName("paymentCodeBase64")
    private String paymentCodeBase64;

    public PixGenerateResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getPaymentCodeBase64() {
        return paymentCodeBase64;
    }

    public void setPaymentCodeBase64(String paymentCodeBase64) {
        this.paymentCodeBase64 = paymentCodeBase64;
    }

    @Override
    public String toString() {
        return "PixGenerateResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", idTransaction='" + idTransaction + '\'' +
                ", paymentCode='" + (paymentCode != null ? paymentCode.substring(0, Math.min(30, paymentCode.length())) + "..." : "null") + '\'' +
                '}';
    }
}
