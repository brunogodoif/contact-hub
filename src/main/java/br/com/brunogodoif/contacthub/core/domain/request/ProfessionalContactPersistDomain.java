package br.com.brunogodoif.contacthub.core.domain.request;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ProfessionalContactPersistenceRequest;
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Data
public class ProfessionalContactPersistDomain {

    private UUID id;
    private ContactTypeEnum type;
    private String contact;
    private Boolean active;

    public ProfessionalContactPersistDomain(String id, ContactTypeEnum type, String contact, Boolean active) {
        UuidValidator.isValidUUID(id);
        this.id = UUID.fromString(id);
        this.type = type;
        this.contact = contact;
        this.active = active;
        validate();
    }

    public ProfessionalContactPersistDomain(ContactTypeEnum type, String contact, Boolean active) {
        this.type = type;
        this.contact = contact;
        this.active = active;
        validate();
    }

    public void validate() {

        if (type == null) {
            throw new ContactDomainException("Contact type cannot be null");
        }

        if (StringUtils.isEmpty(contact)) {
            throw new ContactDomainException("Contact information cannot be null or empty");
        }

        if (active == null) {
            throw new ContactDomainException("Contact professionalId cannot be null or empty");
        }

    }

    public static ProfessionalContactPersistDomain toDomain(ProfessionalContactPersistenceRequest professionalContactPersistenceRequest) {

        if (!StringUtils.isEmpty(professionalContactPersistenceRequest.id())) {
            return new ProfessionalContactPersistDomain(
                    professionalContactPersistenceRequest.id(),
                    ContactTypeEnum.valueOf(professionalContactPersistenceRequest.type()),
                    professionalContactPersistenceRequest.contact(),
                    professionalContactPersistenceRequest.active()
            );
        }

        return new ProfessionalContactPersistDomain(
                ContactTypeEnum.valueOf(professionalContactPersistenceRequest.type()),
                professionalContactPersistenceRequest.contact(),
                professionalContactPersistenceRequest.active()
        );


    }

}