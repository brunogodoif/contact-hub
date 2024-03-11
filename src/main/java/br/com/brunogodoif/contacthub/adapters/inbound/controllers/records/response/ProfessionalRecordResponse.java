package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public record ProfessionalRecordResponse(
        String id,
        String name,
        String role,
        Set<ContactRecord> contacts,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt,
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
