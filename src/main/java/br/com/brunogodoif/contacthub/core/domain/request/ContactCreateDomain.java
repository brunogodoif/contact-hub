package br.com.brunogodoif.contacthub.core.domain.request;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ContactPersistenceRequest;
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ContactCreateDomain {

    private ContactTypeEnum type;
    private String contact;
    private UUID professionalId;
    private Boolean active;

    public ContactCreateDomain(ContactTypeEnum type, String contact, String professionalId, Boolean active) {
        this.type = type;
        this.contact = contact;
        UuidValidator.isValidUUID(professionalId);
        this.professionalId = UUID.fromString(professionalId);
        this.active = active;
        validate();
    }


    public void validate() {
        if (type == null)
            throw new ContactDomainException("Contact type cannot be null");

        if (contact == null || contact.isEmpty())
            throw new ContactDomainException("Contact information cannot be null or empty");

    }

    public static ContactCreateDomain toDomain(ContactPersistenceRequest contactPersistenceRequest) {
        return new ContactCreateDomain(
                ContactTypeEnum.valueOf(contactPersistenceRequest.type()),
                contactPersistenceRequest.contact(),
                contactPersistenceRequest.professionalId(),
                contactPersistenceRequest.active()
        );
    }
}
