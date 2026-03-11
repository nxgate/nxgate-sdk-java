package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Resposta da consulta de transação.
 */
public class TransactionResponse {

    @SerializedName("idTransaction")
    private String idTransaction;

    @SerializedName("status")
    private String status;

    @SerializedName("amount")
    private Double amount;

    @SerializedName("paidAt")
    private String paidAt;

    @SerializedName("endToEnd")
    private String endToEnd;

    @SerializedName("type")
    private String type;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("magic_id")
    private String magicId;

    @SerializedName("fee")
    private Double fee;

    public TransactionResponse() {
    }

    public String getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(String idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    public String getEndToEnd() {
        return endToEnd;
    }

    public void setEndToEnd(String endToEnd) {
        this.endToEnd = endToEnd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "idTransaction='" + idTransaction + '\'' +
                ", status='" + status + '\'' +
                ", amount=" + amount +
                ", paidAt='" + paidAt + '\'' +
                ", endToEnd='" + endToEnd + '\'' +
                '}';
    }
}
