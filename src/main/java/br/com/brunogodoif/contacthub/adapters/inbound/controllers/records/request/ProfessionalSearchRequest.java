package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProfessionalSearchRequest(
        @Schema(description = "Search professional by name")
        String name,

        @Schema(description = "Search professional by role")
        String role,

        @Schema(description = "Search professional by start created date")
        String startCreatedAt,

        @Schema(description = "Search professional by start created date")
        String endCreatedAt
) {
}
