package br.com.brunogodoif.contacthub.core.domain.request;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ProfessionalPersistenceRequest;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class ProfessionalUpdateDomain {

    private UUID id;
    private String name;
    private String role;
    private Date birthDate;
    private List<ProfessionalContactPersistDomain> contacts;

    public ProfessionalUpdateDomain(String id, String name, String role, Date birthDate, List<ProfessionalContactPersistDomain> contacts) {
        UuidValidator.isValidUUID(id);
        this.id = UUID.fromString(id);
        this.name = name;
        this.role = role;
        this.birthDate = birthDate;
        this.contacts = contacts;
        validate();
    }


    public void validate() {
        if (StringUtils.isEmpty(name)) {
            throw new ProfessionalDomainException("Name cannot be null or empty");
        }
        if (StringUtils.isEmpty(role)) {
            throw new ProfessionalDomainException("Role cannot be null or empty");
        }
    }

    public static ProfessionalUpdateDomain toDomain(ProfessionalPersistenceRequest professionalPersistenceRequest, String id) {
        return new ProfessionalUpdateDomain(
                id,
                professionalPersistenceRequest.name(),
                professionalPersistenceRequest.role(),
                professionalPersistenceRequest.birthDate(),
                professionalPersistenceRequest.contacts().stream()
                        .map(ProfessionalContactPersistDomain::toDomain).toList()
        );
    }


}