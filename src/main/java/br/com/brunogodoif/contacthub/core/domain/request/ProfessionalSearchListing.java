package br.com.brunogodoif.contacthub.core.domain.request;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ProfessionalSearchRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Data
public class ProfessionalSearchListing {
    @Schema(description = "Search professional by name")
    private String name;

    @Schema(description = "Search professional by role")
    private String role;

    @Schema(description = "Search professional by start created date")
    private LocalDate startCreatedAt;

    @Schema(description = "Search professional by end created date")
    private LocalDate endCreatedAt;

    public ProfessionalSearchListing(String name, String role, String startCreatedAt, String endCreatedAt) {
        this.name = name;
        this.role = role;

        if ((startCreatedAt != null && endCreatedAt == null) ||
                (startCreatedAt == null && endCreatedAt != null)) {
            throw new IllegalArgumentException("Both startCreatedAt and endCreatedAt should be provided or left empty.");
        }

        try {
            this.startCreatedAt = startCreatedAt != null ? LocalDate.parse(startCreatedAt) : null;
            this.endCreatedAt = endCreatedAt != null ? LocalDate.parse(endCreatedAt) : null;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please provide dates in yyyy-MM-dd format.");
        }
    }

    public ProfessionalSearchListing(ProfessionalSearchRequest searchRequest) {
        this(searchRequest.name(), searchRequest.role(), searchRequest.startCreatedAt(), searchRequest.endCreatedAt());
    }
}