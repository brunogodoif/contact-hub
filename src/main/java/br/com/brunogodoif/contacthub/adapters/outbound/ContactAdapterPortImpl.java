package br.com.brunogodoif.contacthub.adapters.outbound;

import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.DatabaseAccessException;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.ContactRepository;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain;
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ContactAdapterPortImpl implements ContactAdapterPort {

    private final ContactRepository contactRepository;
    private final ProfessionalAdapterPort professionalAdapter;

    public ContactAdapterPortImpl(ContactRepository contactRepository, ProfessionalAdapterPort professionalAdapter) {
        this.contactRepository = contactRepository;
        this.professionalAdapter = professionalAdapter;
    }

    @Override
    public ContactDomain create(ContactCreateDomain contactToCreate) {
        try {
            ContactEntity contactSaved = contactRepository.save(toEntity(contactToCreate));
            return ContactDomain.toDomain(contactSaved);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public boolean update(ContactUpdateDomain contactToUpdate) {
        Optional<ContactEntity> contactEntity = getExistingContact(contactToUpdate.getId());

        if (contactEntity.isEmpty())
            return false;

        ContactEntity existingContact = contactEntity.get();
        existingContact.setType(contactToUpdate.getType());
        existingContact.setContact(contactToUpdate.getContact());
        existingContact.setActive(contactToUpdate.getActive());

        try {
            contactRepository.save(existingContact);
            return true;
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }

    }

    @Override
    public List<ContactDomain> listAllContactsByProfessionalId(UUID professionalId) {

        List<ContactEntity> all = contactRepository.findAllByProfessionalId(professionalId, Sort.by(Sort.Direction.ASC, "type"));

        if (all.isEmpty()) {
            Collections.emptyList();
        }

        return all
                .stream()
                .map(ContactDomain::toDomain)
                .toList();

    }

    @Override
    public Optional<ContactDomain> getById(UUID id) {
        Optional<ContactEntity> contactEntity = getExistingContact(id);
        if (contactEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ContactDomain.toDomain(contactEntity.get()));
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            contactRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    private Optional<ContactEntity> getExistingContact(UUID id) {
        try {
            return contactRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    public ContactEntity toEntity(ContactCreateDomain contactCreateDomain) {

        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setType(contactCreateDomain.getType());
        contactEntity.setContact(contactCreateDomain.getContact());
        contactEntity.setActive(contactCreateDomain.getActive());

        if (contactCreateDomain.getProfessionalId() != null) {
            ProfessionalEntity professionalEntity = new ProfessionalEntity();
            professionalEntity.setId(contactCreateDomain.getProfessionalId());
            contactEntity.setProfessional(professionalEntity);
        }

        return contactEntity;
    }


}
