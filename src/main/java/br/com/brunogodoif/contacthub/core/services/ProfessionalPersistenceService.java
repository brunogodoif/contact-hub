package br.com.brunogodoif.contacthub.core.services;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalUpdateDomain;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalPersistenceServicePort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import br.com.brunogodoif.contacthub.core.services.exceptions.ProfessionalNotFoundException;
import br.com.brunogodoif.contacthub.core.services.exceptions.ProfessionalUpdatedException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;

import java.util.Optional;
import java.util.UUID;

public class ProfessionalPersistenceService implements ProfessionalPersistenceServicePort {

    private final ProfessionalAdapterPort professionalAdapter;

    public ProfessionalPersistenceService(ProfessionalAdapterPort professionalAdapter) {
        this.professionalAdapter = professionalAdapter;
    }

    @Override
    public ProfessionalDomain create(ProfessionalCreateDomain professionalCreateDomain) {
        return professionalAdapter.create(professionalCreateDomain);
    }

    public ProfessionalDomain update(ProfessionalUpdateDomain professionalUpdateDomain) {
        boolean updated = professionalAdapter.update(professionalUpdateDomain);

        if (!updated) {
            throw new ProfessionalUpdatedException("Failed to update professional with id [" + professionalUpdateDomain.getId().toString() + "]");
        }

        Optional<ProfessionalDomain> professionalDomainOptional = professionalAdapter.getById(professionalUpdateDomain.getId());

        if (professionalDomainOptional.isPresent()) {
            return professionalDomainOptional.get();
        } else {
            throw new ProfessionalNotFoundException("Failed to get professional data with id [" + professionalUpdateDomain.getId().toString() + "] after update");
        }
    }

    @Override
    public boolean deleteById(String id) {
        UuidValidator.isValidUUID(id);

        UUID uuid = UUID.fromString(id);

        Optional<ProfessionalDomain> professionalDomainOptional = professionalAdapter.getById(uuid);

        if (professionalDomainOptional.isEmpty())
            throw new ProfessionalNotFoundException("Professional with id [" + id + "] not found");

        return professionalAdapter.deleteById(uuid);
    }
}

