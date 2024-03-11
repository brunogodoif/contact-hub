package br.com.brunogodoif.contacthub.core.services;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactPersistenceServicePort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import br.com.brunogodoif.contacthub.core.services.exceptions.ContactNotFoundException;
import br.com.brunogodoif.contacthub.core.services.exceptions.ContactUpdatedException;
import br.com.brunogodoif.contacthub.core.services.exceptions.ProfessionalNotFoundException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;

import java.util.Optional;
import java.util.UUID;

public class ContactPersistenceService implements ContactPersistenceServicePort {

    private final ContactAdapterPort contactAdapter;
    private final ProfessionalAdapterPort professionalAdapterPort;

    public ContactPersistenceService(ContactAdapterPort contactAdapter, ProfessionalAdapterPort professionalAdapterPort) {
        this.contactAdapter = contactAdapter;
        this.professionalAdapterPort = professionalAdapterPort;
    }

    @Override
    public ContactDomain create(ContactCreateDomain contactCreateDomain) {
        professionalExists(contactCreateDomain.getProfessionalId());
        return contactAdapter.create(contactCreateDomain);
    }

    @Override
    public ContactDomain update(ContactUpdateDomain contactUpdateDomain) {
        professionalExists(contactUpdateDomain.getProfessionalId());

        contactExists(contactUpdateDomain.getId());

        boolean updated = contactAdapter.update(contactUpdateDomain);

        if (!updated) {
            throw new ContactUpdatedException("Failed to update contact with id [" + contactUpdateDomain.getId().toString() + "]");
        }

        Optional<ContactDomain> contactDomainOptional = contactAdapter.getById(contactUpdateDomain.getId());
        if (contactDomainOptional.isPresent()) {
            return contactDomainOptional.get();
        } else {
            throw new ContactNotFoundException("Failed to get contact data with id [" + contactUpdateDomain.getId().toString() + "] after update");
        }
    }

    @Override
    public boolean deleteById(String id) {
        UuidValidator.isValidUUID(id);
        UUID uuid = UUID.fromString(id);

        contactExists(uuid);

        return contactAdapter.deleteById(uuid);
    }

    private void professionalExists(UUID professionalId) {
        Optional<ProfessionalDomain> existingprofessionalExists = professionalAdapterPort.getById(professionalId);

        if (existingprofessionalExists.isEmpty())
            throw new ProfessionalNotFoundException("There is no professional registered for the id [" + professionalId.toString() + "]");
    }

    private void contactExists(UUID contactId) {
        Optional<ContactDomain> contactDomainExists = contactAdapter.getById(contactId);
        if (contactDomainExists.isEmpty())
            throw new ContactNotFoundException("Contact with id [" + contactId.toString() + "] not found");
    }

}
