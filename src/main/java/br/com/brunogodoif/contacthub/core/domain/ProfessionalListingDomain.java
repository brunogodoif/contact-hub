package br.com.brunogodoif.contacthub.core.domain;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto.ProfessionalEntityListingRecord;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProfessionalListingDomain {

    private UUID id;
    private String name;
    private String role;
    private Long totalContacts;
    private LocalDateTime createdAt;

    public ProfessionalListingDomain(UUID id, String name, String role, Long totalContacts, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.totalContacts = totalContacts;
        this.createdAt = createdAt;
        validate();
    }

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new ProfessionalDomainException("Name cannot be null or empty");
        }
        if (role == null || role.isEmpty()) {
            throw new ProfessionalDomainException("Role cannot be null or empty");
        }
    }

    public static ProfessionalListingDomain toDomain(ProfessionalEntityListingRecord professionalEntityListingRecord) {

        return new ProfessionalListingDomain(
                professionalEntityListingRecord.id(),
                professionalEntityListingRecord.name(),
                professionalEntityListingRecord.role(),
                professionalEntityListingRecord.totalContacts(),
                professionalEntityListingRecord.createdAt()
        );
    }

}
