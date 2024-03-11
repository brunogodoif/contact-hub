package br.com.brunogodoif.contacthub.adapters.outbound;

import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.DatabaseAccessException;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.ProfessionalRepository;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto.ProfessionalEntityListingRecord;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalContactPersistDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalUpdateDomain;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ProfessionalsAdapterPortImpl implements ProfessionalAdapterPort {

    private final ProfessionalRepository professionalRepository;

    public ProfessionalsAdapterPortImpl(ProfessionalRepository professionalRepository) {
        this.professionalRepository = professionalRepository;
    }

    @Override
    public ProfessionalDomain create(ProfessionalCreateDomain professionalToCreate) {
        try {
            ProfessionalEntity professionalEntity = toProfessionalEntity(professionalToCreate);

            if (!professionalToCreate.getContacts().isEmpty()) {
                Set<ContactEntity> contactEntities = professionalToCreate.getContacts()
                        .stream()
                        .map(this::toContactEntity)
                        .map(contactEntity -> associateContactWithProfessional(contactEntity, professionalEntity))
                        .collect(Collectors.toSet());
                professionalEntity.setContacts(new HashSet<>(contactEntities));
            }

            ProfessionalEntity professionalSaved = professionalRepository.save(professionalEntity);
            return ProfessionalDomain.toDomain(professionalSaved);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    @Override
    public boolean update(ProfessionalUpdateDomain professionalToUpdate) {
        Optional<ProfessionalEntity> existingProfessionalOptional = getExistingProfessional(professionalToUpdate.getId());

        if (existingProfessionalOptional.isEmpty())
            return false;

        ProfessionalEntity existingProfessional = existingProfessionalOptional.get();
        existingProfessional.setName(professionalToUpdate.getName());
        existingProfessional.setRole(professionalToUpdate.getRole());
        existingProfessional.setBirthDate(professionalToUpdate.getBirthDate());

        Set<ContactEntity> updatedContacts = new HashSet<>();

        for (ProfessionalContactPersistDomain contactDomain : professionalToUpdate.getContacts()) {
            if (contactDomain.getId() != null) {
                existingProfessional.getContacts().stream()
                        .filter(contact -> contact.getId().equals(contactDomain.getId()))
                        .findFirst()
                        .ifPresent(existingContact -> {
                            existingContact.setType(contactDomain.getType());
                            existingContact.setContact(contactDomain.getContact());
                            existingContact.setActive(contactDomain.getActive());
                            updatedContacts.add(existingContact);
                        });
            } else {
                ContactEntity newContact = toContactEntity(contactDomain);
                newContact.setProfessional(existingProfessional);
                updatedContacts.add(newContact);
            }
        }

        existingProfessional.setContacts(updatedContacts);

        try {
            professionalRepository.save(existingProfessional);
            return true;
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }

    }

    @Override
    public Page<ProfessionalListingDomain> paginate(PaginationRequest paginationRequest) {
        Pageable pageable = PageRequest.of(paginationRequest.getPageNumber() - 1, paginationRequest.getPageSize());
        Page<ProfessionalEntityListingRecord> all;

        try {
            all = professionalRepository.findAllWithContactCount(pageable);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }

        if (all.isEmpty()) {
            return Page.empty();
        }

        List<ProfessionalListingDomain> professionalListingDomains = all
                .stream()
                .map(ProfessionalListingDomain::toDomain)
                .toList();

        return new PageImpl<>(professionalListingDomains, pageable, all.getTotalElements());
    }

    @Override
    public Optional<ProfessionalDomain> getById(UUID id) {
        Optional<ProfessionalEntity> professionalEntity = getExistingProfessional(id);
        if (professionalEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ProfessionalDomain.toDomain(professionalEntity.get()));
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            professionalRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    private Optional<ProfessionalEntity> getExistingProfessional(UUID id) {
        try {
            return professionalRepository.findById(id);
        } catch (Exception e) {
            throw new DatabaseAccessException(e);
        }
    }

    private static ProfessionalEntity toProfessionalEntity(ProfessionalCreateDomain professionalToCreate) {
        ProfessionalEntity professionalEntity = new ProfessionalEntity();
        professionalEntity.setName(professionalToCreate.getName());
        professionalEntity.setRole(professionalToCreate.getRole());
        professionalEntity.setBirthDate(professionalToCreate.getBirthDate());
        return professionalEntity;
    }

    private ContactEntity toContactEntity(ProfessionalContactPersistDomain professionalContactPersistDomain) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setType(professionalContactPersistDomain.getType());
        contactEntity.setContact(professionalContactPersistDomain.getContact());
        contactEntity.setActive(professionalContactPersistDomain.getActive());
        return contactEntity;
    }

    private ContactEntity associateContactWithProfessional(ContactEntity contactEntity, ProfessionalEntity professionalEntity) {
        contactEntity.setProfessional(professionalEntity);
        return contactEntity;
    }

}
