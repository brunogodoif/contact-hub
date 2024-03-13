package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContactPersistenceRequest(
        @Schema(description = "Contact type", example = "EMAIL")
        String type,
        @Schema(description = "Contact information", example = "example@example.com")
        String contact,
        @Schema(description = "Professional ID", example = "123456")
        String professionalId,
        @Schema(description = "Flag indicating if contact is active", example = "true")
        Boolean active
) {
}