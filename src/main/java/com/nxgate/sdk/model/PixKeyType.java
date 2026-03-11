package com.nxgate.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * Tipos de chave PIX suportados.
 */
public enum PixKeyType {

    @SerializedName("CPF")
    CPF("CPF"),

    @SerializedName("CNPJ")
    CNPJ("CNPJ"),

    @SerializedName("PHONE")
    PHONE("PHONE"),

    @SerializedName("EMAIL")
    EMAIL("EMAIL"),

    @SerializedName("RANDOM")
    RANDOM("RANDOM");

    private final String value;

    PixKeyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Converte uma string para o enum correspondente.
     *
     * @param value valor string
     * @return PixKeyType correspondente
     * @throws IllegalArgumentException se o valor não for reconhecido
     */
    public static PixKeyType fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Tipo de chave PIX não pode ser nulo");
        }
        for (PixKeyType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de chave PIX desconhecido: " + value);
    }
}
