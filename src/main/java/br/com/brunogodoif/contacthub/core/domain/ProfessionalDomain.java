package br.com.brunogodoif.contacthub.core.domain;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ProfessionalDomain {

    private UUID id;
    private String name;
    private String role;
    private Set<ContactDomain> contacts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProfessionalDomain(UUID id, String name, String role, Set<ContactDomain> contacts, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.contacts = contacts;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new ProfessionalDomainException("Name cannot be null or empty");
        }
        if (role == null || role.isEmpty()) {
            throw new ProfessionalDomainException("Role cannot be null or empty");
        }
        if (createdAt == null || updatedAt == null) {
            throw new ProfessionalDomainException("Timestamps cannot be null");
        }
    }

    public static ProfessionalDomain toDomain(ProfessionalEntity professionalEntity) {
        return toDomain(professionalEntity, true);
    }

    public static ProfessionalDomain toDomain(ProfessionalEntity professionalEntity, boolean includeContacts) {
        Set<ContactDomain> contactDomains = includeContacts ?
                professionalEntity.getContacts().stream()
                        .map(ContactDomain::toDomain)
                        .collect(Collectors.toSet()) :
                Collections.emptySet();

        return new ProfessionalDomain(
                professionalEntity.getId(),
                professionalEntity.getName(),
                professionalEntity.getRole(),
                contactDomains,
                professionalEntity.getCreatedAt(),
                professionalEntity.getUpdatedAt()
        );
    }

}
