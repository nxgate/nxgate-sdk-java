package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Resposta do saque PIX (cash-out).
 */
public class PixWithdrawResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("internalreference")
    private String internalReference;

    public PixWithdrawResponse() {
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

    public String getInternalReference() {
        return internalReference;
    }

    public void setInternalReference(String internalReference) {
        this.internalReference = internalReference;
    }

    @Override
    public String toString() {
        return "PixWithdrawResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", internalReference='" + internalReference + '\'' +
                '}';
    }
}
