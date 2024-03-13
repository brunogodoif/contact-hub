package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Schema(description = "Response representing a professional")
public record ProfessionalRecordResponse(
        @Schema(description = "Professional ID")
        String id,
        @Schema(description = "Professional name")
        String name,
        @Schema(description = "Professional role")
        String role,
        @Schema(description = "Set of contact records")
        Set<ContactRecord> contacts,
        @Schema(description = "Creation timestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt,
        @Schema(description = "Update timestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime updatedAt

) {
    public static ProfessionalRecordResponse fromDomain(ProfessionalDomain professionalDomain) {

        Set<ContactRecord> contactDomains = !professionalDomain.getContacts().isEmpty() ? professionalDomain.getContacts().stream()
                .map(ContactRecord::fromDomain)
                .collect(Collectors.toSet()) :
                Collections.emptySet();

        return new ProfessionalRecordResponse(
                professionalDomain.getId().toString(),
                professionalDomain.getName(),
                professionalDomain.getRole(),
                contactDomains,
                professionalDomain.getCreatedAt(),
                professionalDomain.getUpdatedAt()
        );
    }
}
