package com.nxgate.sdk;

/**
 * Exceção customizada para erros da API NXGATE.
 * Contém código HTTP, título, descrição e status retornados pela API.
 */
public class NXGateException extends Exception {

    private final int code;
    private final String title;
    private final String description;
    private final String status;

    /**
     * Cria uma nova NXGateException.
     *
     * @param code        código HTTP da resposta
     * @param title       título do erro
     * @param description descrição detalhada do erro
     * @param status      status retornado pela API
     */
    public NXGateException(int code, String title, String description, String status) {
        super(buildMessage(code, title, description, status));
        this.code = code;
        this.title = title;
        this.description = description;
        this.status = status;
    }

    /**
     * Cria uma NXGateException a partir de uma causa raiz.
     *
     * @param message mensagem de erro
     * @param cause   causa raiz
     */
    public NXGateException(String message, Throwable cause) {
        super(message, cause);
        this.code = 0;
        this.title = "SDK Error";
        this.description = message;
        this.status = "error";
    }

    /**
     * Cria uma NXGateException com mensagem simples.
     *
     * @param message mensagem de erro
     */
    public NXGateException(String message) {
        super(message);
        this.code = 0;
        this.title = "SDK Error";
        this.description = message;
        this.status = "error";
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    private static String buildMessage(int code, String title, String description, String status) {
        StringBuilder sb = new StringBuilder();
        sb.append("NXGateException [").append(code).append("]");
        if (title != null && !title.isEmpty()) {
            sb.append(" ").append(title);
        }
        if (description != null && !description.isEmpty()) {
            sb.append(": ").append(description);
        }
        if (status != null && !status.isEmpty()) {
            sb.append(" (status=").append(status).append(")");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
