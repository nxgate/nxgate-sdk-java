package com.nxgate.sdk;

/**
 * Configuração do SDK NXGATE com padrão Builder.
 */
public class NXGateConfig {

    private static final String DEFAULT_BASE_URL = "https://api.nxgate.com.br";

    private final String clientId;
    private final String clientSecret;
    private final String hmacSecret;
    private final String baseUrl;
    private final int maxRetries;
    private final long connectTimeoutMs;
    private final long requestTimeoutMs;

    private NXGateConfig(Builder builder) {
        this.clientId = builder.clientId;
        this.clientSecret = builder.clientSecret;
        this.hmacSecret = builder.hmacSecret;
        this.baseUrl = builder.baseUrl;
        this.maxRetries = builder.maxRetries;
        this.connectTimeoutMs = builder.connectTimeoutMs;
        this.requestTimeoutMs = builder.requestTimeoutMs;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getHmacSecret() {
        return hmacSecret;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public long getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public long getRequestTimeoutMs() {
        return requestTimeoutMs;
    }

    public boolean hasHmacSecret() {
        return hmacSecret != null && !hmacSecret.isEmpty();
    }

    /**
     * Builder para NXGateConfig.
     */
    public static class Builder {
        private String clientId;
        private String clientSecret;
        private String hmacSecret;
        private String baseUrl = DEFAULT_BASE_URL;
        private int maxRetries = 2;
        private long connectTimeoutMs = 10_000;
        private long requestTimeoutMs = 30_000;

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder hmacSecret(String hmacSecret) {
            this.hmacSecret = hmacSecret;
            return this;
        }

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder maxRetries(int maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public Builder connectTimeoutMs(long connectTimeoutMs) {
            this.connectTimeoutMs = connectTimeoutMs;
            return this;
        }

        public Builder requestTimeoutMs(long requestTimeoutMs) {
            this.requestTimeoutMs = requestTimeoutMs;
            return this;
        }

        /**
         * Constrói a instância NXGateConfig validando campos obrigatórios.
         *
         * @return instância configurada
         * @throws IllegalArgumentException se clientId ou clientSecret estiverem ausentes
         */
        public NXGateConfig build() {
            if (clientId == null || clientId.isEmpty()) {
                throw new IllegalArgumentException("clientId é obrigatório");
            }
            if (clientSecret == null || clientSecret.isEmpty()) {
                throw new IllegalArgumentException("clientSecret é obrigatório");
            }
            return new NXGateConfig(this);
        }
    }
}
