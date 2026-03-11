package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Request para saque PIX (cash-out).
 */
public class PixWithdrawRequest {

    @SerializedName("valor")
    private final Double valor;

    @SerializedName("chave_pix")
    private final String chavePix;

    @SerializedName("tipo_chave")
    private final String tipoChave;

    @SerializedName("documento")
    private final String documento;

    @SerializedName("webhook")
    private final String webhook;

    @SerializedName("magic_id")
    private final String magicId;

    @SerializedName("api_key")
    private final String apiKey;

    private PixWithdrawRequest(Builder builder) {
        this.valor = builder.valor;
        this.chavePix = builder.chavePix;
        this.tipoChave = builder.tipoChave != null ? builder.tipoChave.getValue() : null;
        this.documento = builder.documento;
        this.webhook = builder.webhook;
        this.magicId = builder.magicId;
        this.apiKey = builder.apiKey;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Double getValor() {
        return valor;
    }

    public String getChavePix() {
        return chavePix;
    }

    public String getTipoChave() {
        return tipoChave;
    }

    public String getDocumento() {
        return documento;
    }

    public String getWebhook() {
        return webhook;
    }

    public String getMagicId() {
        return magicId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public static class Builder {
        private Double valor;
        private String chavePix;
        private PixKeyType tipoChave;
        private String documento;
        private String webhook;
        private String magicId;
        private String apiKey;

        public Builder valor(double valor) {
            this.valor = valor;
            return this;
        }

        public Builder chavePix(String chavePix) {
            this.chavePix = chavePix;
            return this;
        }

        public Builder tipoChave(PixKeyType tipoChave) {
            this.tipoChave = tipoChave;
            return this;
        }

        public Builder documento(String documento) {
            this.documento = documento;
            return this;
        }

        public Builder webhook(String webhook) {
            this.webhook = webhook;
            return this;
        }

        public Builder magicId(String magicId) {
            this.magicId = magicId;
            return this;
        }

        public Builder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        /**
         * Constrói o request validando campos obrigatórios.
         *
         * @return PixWithdrawRequest
         * @throws IllegalArgumentException se campos obrigatórios estiverem ausentes
         */
        public PixWithdrawRequest build() {
            if (valor == null || valor <= 0) {
                throw new IllegalArgumentException("valor é obrigatório e deve ser maior que zero");
            }
            if (chavePix == null || chavePix.isEmpty()) {
                throw new IllegalArgumentException("chavePix é obrigatório");
            }
            if (tipoChave == null) {
                throw new IllegalArgumentException("tipoChave é obrigatório");
            }
            return new PixWithdrawRequest(this);
        }
    }
}
