package br.com.brunogodoif.contacthub.core.domain;

import java.util.Arrays;

public enum ContactTypeEnum {
    PHONE("TELEFONE FIXO"),
    MOBILE_PHONE("TELEFONE CELULAR"),
    OFFICE_PHONE("TELEFONE ESCRITÓRIO"),
    EMAIL("EMAIL"),
    LINKEDIN("LINKEDIN"),
    INSTAGRAM("INSTAGRAM"),
    FACEBOOK("FACEBOOK");

    private final String description;

    ContactTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static void validate(String type) {
        try {
            ContactTypeEnum.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Valor inválido para ContactType. Valores permitidos: " + Arrays.toString(ContactTypeEnum.values()), e);
        }
    }
}

