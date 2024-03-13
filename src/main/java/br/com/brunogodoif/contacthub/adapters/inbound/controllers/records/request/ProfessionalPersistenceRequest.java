package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

@Schema(description = "Request representing data for creating or updating a professional")
public record ProfessionalPersistenceRequest(
        @Schema(description = "Professional name")
        String name,
        @Schema(description = "Professional role")
        String role,
        @Schema(description = "Professional birth date")
        Date birthDate,
        @Schema(description = "List of contacts associated with the professional")
        List<ProfessionalContactPersistenceRequest> contacts
)  {
}