package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProfessionalSearchRequest(
        @Schema(description = "Search professional by name", example = " ")
        @Parameter(required = false)
        String name,

        @Schema(description = "Search professional by role", example = " ")
        @Parameter(required = false)
        String role,

        @Schema(description = "Search professional by start created date", example = " ")
        @Parameter(required = false)
        String startCreatedAt,

        @Schema(description = "Search professional by start created date", example = " ")
        @Parameter(required = false)
        String endCreatedAt
) {
}
