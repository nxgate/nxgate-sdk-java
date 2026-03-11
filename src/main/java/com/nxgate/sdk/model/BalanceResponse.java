package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Resposta da consulta de saldo.
 */
public class BalanceResponse {

    @SerializedName("balance")
    private Double balance;

    @SerializedName("blocked")
    private Double blocked;

    @SerializedName("available")
    private Double available;

    public BalanceResponse() {
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getBlocked() {
        return blocked;
    }

    public void setBlocked(Double blocked) {
        this.blocked = blocked;
    }

    public Double getAvailable() {
        return available;
    }

    public void setAvailable(Double available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "BalanceResponse{" +
                "balance=" + balance +
                ", blocked=" + blocked +
                ", available=" + available +
                '}';
    }
}
