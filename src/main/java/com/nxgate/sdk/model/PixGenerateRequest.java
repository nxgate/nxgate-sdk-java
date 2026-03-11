package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Request para geração de cobrança PIX (cash-in).
 */
public class PixGenerateRequest {

    @SerializedName("valor")
    private final Double valor;

    @SerializedName("nome_pagador")
    private final String nomePagador;

    @SerializedName("documento_pagador")
    private final String documentoPagador;

    @SerializedName("forcar_pagador")
    private final Boolean forcarPagador;

    @SerializedName("email_pagador")
    private final String emailPagador;

    @SerializedName("celular")
    private final String celular;

    @SerializedName("descricao")
    private final String descricao;

    @SerializedName("webhook")
    private final String webhook;

    @SerializedName("magic_id")
    private final String magicId;

    @SerializedName("api_key")
    private final String apiKey;

    @SerializedName("split_users")
    private final List<SplitUser> splitUsers;

    private PixGenerateRequest(Builder builder) {
        this.valor = builder.valor;
        this.nomePagador = builder.nomePagador;
        this.documentoPagador = builder.documentoPagador;
        this.forcarPagador = builder.forcarPagador;
        this.emailPagador = builder.emailPagador;
        this.celular = builder.celular;
        this.descricao = builder.descricao;
        this.webhook = builder.webhook;
        this.magicId = builder.magicId;
        this.apiKey = builder.apiKey;
        this.splitUsers = builder.splitUsers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Double getValor() {
        return valor;
    }

    public String getNomePagador() {
        return nomePagador;
    }

    public String getDocumentoPagador() {
        return documentoPagador;
    }

    public Boolean getForcarPagador() {
        return forcarPagador;
    }

    public String getEmailPagador() {
        return emailPagador;
    }

    public String getCelular() {
        return celular;
    }

    public String getDescricao() {
        return descricao;
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

    public List<SplitUser> getSplitUsers() {
        return splitUsers;
    }

    /**
     * Representa um usuário de split de pagamento.
     */
    public static class SplitUser {
        @SerializedName("username")
        private final String username;

        @SerializedName("percentage")
        private final Double percentage;

        public SplitUser(String username, Double percentage) {
            this.username = username;
            this.percentage = percentage;
        }

        public String getUsername() {
            return username;
        }

        public Double getPercentage() {
            return percentage;
        }
    }

    public static class Builder {
        private Double valor;
        private String nomePagador;
        private String documentoPagador;
        private Boolean forcarPagador;
        private String emailPagador;
        private String celular;
        private String descricao;
        private String webhook;
        private String magicId;
        private String apiKey;
        private List<SplitUser> splitUsers;

        public Builder valor(double valor) {
            this.valor = valor;
            return this;
        }

        public Builder nomePagador(String nomePagador) {
            this.nomePagador = nomePagador;
            return this;
        }

        public Builder documentoPagador(String documentoPagador) {
            this.documentoPagador = documentoPagador;
            return this;
        }

        public Builder forcarPagador(Boolean forcarPagador) {
            this.forcarPagador = forcarPagador;
            return this;
        }

        public Builder emailPagador(String emailPagador) {
            this.emailPagador = emailPagador;
            return this;
        }

        public Builder celular(String celular) {
            this.celular = celular;
            return this;
        }

        public Builder descricao(String descricao) {
            this.descricao = descricao;
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

        public Builder splitUsers(List<SplitUser> splitUsers) {
            this.splitUsers = splitUsers;
            return this;
        }

        /**
         * Constrói o request validando campos obrigatórios.
         *
         * @return PixGenerateRequest
         * @throws IllegalArgumentException se campos obrigatórios estiverem ausentes
         */
        public PixGenerateRequest build() {
            if (valor == null || valor <= 0) {
                throw new IllegalArgumentException("valor é obrigatório e deve ser maior que zero");
            }
            if (nomePagador == null || nomePagador.isEmpty()) {
                throw new IllegalArgumentException("nomePagador é obrigatório");
            }
            if (documentoPagador == null || documentoPagador.isEmpty()) {
                throw new IllegalArgumentException("documentoPagador é obrigatório");
            }
            return new PixGenerateRequest(this);
        }
    }
}
