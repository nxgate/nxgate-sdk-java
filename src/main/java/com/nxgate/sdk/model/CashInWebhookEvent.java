package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Evento de webhook para transações cash-in (recebimentos PIX).
 * <p>
 * Tipos possíveis:
 * <ul>
 *   <li>{@code QR_CODE_COPY_AND_PASTE_PAID} - pagamento confirmado</li>
 *   <li>{@code QR_CODE_COPY_AND_PASTE_REFUNDED} - pagamento estornado</li>
 * </ul>
 */
public class CashInWebhookEvent extends WebhookEvent {

    @SerializedName("data")
    private CashInData data;

    public CashInWebhookEvent() {
    }

    public CashInData getData() {
        return data;
    }

    public void setData(CashInData data) {
        this.data = data;
    }

    /**
     * Dados internos do evento cash-in.
     */
    public static class CashInData {

        @SerializedName("amount")
        private Double amount;

        @SerializedName("status")
        private String status;

        @SerializedName("worked")
        private Boolean worked;

        @SerializedName("tag")
        private String tag;

        @SerializedName("tx_id")
        private String txId;

        @SerializedName("end_to_end")
        private String endToEnd;

        @SerializedName("payment_date")
        private String paymentDate;

        @SerializedName("debtor_name")
        private String debtorName;

        @SerializedName("debtor_document")
        private String debtorDocument;

        @SerializedName("type_document")
        private String typeDocument;

        @SerializedName("magic_id")
        private String magicId;

        @SerializedName("fee")
        private Double fee;

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
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

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTxId() {
            return txId;
        }

        public void setTxId(String txId) {
            this.txId = txId;
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

        public String getDebtorName() {
            return debtorName;
        }

        public void setDebtorName(String debtorName) {
            this.debtorName = debtorName;
        }

        public String getDebtorDocument() {
            return debtorDocument;
        }

        public void setDebtorDocument(String debtorDocument) {
            this.debtorDocument = debtorDocument;
        }

        public String getTypeDocument() {
            return typeDocument;
        }

        public void setTypeDocument(String typeDocument) {
            this.typeDocument = typeDocument;
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
            return "CashInData{" +
                    "amount=" + amount +
                    ", status='" + status + '\'' +
                    ", txId='" + txId + '\'' +
                    ", endToEnd='" + endToEnd + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CashInWebhookEvent{type='" + getType() + "', data=" + data + "}";
    }
}
