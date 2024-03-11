package br.com.brunogodoif.contacthub.core.domain;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ContactDomain {

    private UUID id;
    private ContactTypeEnum type;
    private String contact;
    private UUID professionalId;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ContactDomain(UUID id, ContactTypeEnum type, String contact, UUID professionalId, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.type = type;
        this.contact = contact;
        this.professionalId = professionalId;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    public void validate() {
        if (id == null) {
            throw new ContactDomainException("Contact Id cannot be null");
        }

        if (type == null) {
            throw new ContactDomainException("Contact type cannot be null");
        }

        if (contact == null || contact.isEmpty()) {
            throw new ContactDomainException("Contact information cannot be null or empty");
        }

        if (professionalId == null) {
            throw new ContactDomainException("Contact professionalId cannot be null or empty");
        }

        if (createdAt == null || updatedAt == null) {
            throw new ContactDomainException("Timestamps cannot be null");
        }
    }

    public static ContactDomain toDomain(ContactEntity contactEntity) {
        return new ContactDomain(
                contactEntity.getId(),
                contactEntity.getType(),
                contactEntity.getContact(),
                contactEntity.getProfessional().getId(),
                contactEntity.getActive(),
                contactEntity.getCreatedAt(),
                contactEntity.getUpdatedAt()
        );
    }
}
