package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ProfessionalListingRecordResponse(
        String id,
        String name,
        String role,
        Long totalContacts,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt

) {
    public static ProfessionalListingRecordResponse fromDomain(ProfessionalListingDomain professionalListingDomain) {
        return new ProfessionalListingRecordResponse(
                professionalListingDomain.getId().toString(),
                professionalListingDomain.getName(),
                professionalListingDomain.getRole(),
                professionalListingDomain.getTotalContacts(),
                professionalListingDomain.getCreatedAt()
        );
    }
}
