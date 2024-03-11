package br.com.brunogodoif.contacthub.core.domain.request;

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ProfessionalPersistenceRequest;
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

@Data
public class ProfessionalCreateDomain {

    private String name;
    private String role;
    private Date birthDate;
    private List<ProfessionalContactPersistDomain> contacts;

    public ProfessionalCreateDomain(String name, String role, Date birthDate, List<ProfessionalContactPersistDomain> contacts) {
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

    public static ProfessionalCreateDomain toDomain(ProfessionalPersistenceRequest professionalPersistenceRequest) {
        return new ProfessionalCreateDomain(
                professionalPersistenceRequest.name(),
                professionalPersistenceRequest.role(),
                professionalPersistenceRequest.birthDate(),
                professionalPersistenceRequest.contacts().stream()
                        .map(ProfessionalContactPersistDomain::toDomain).toList()
        );
    }

}