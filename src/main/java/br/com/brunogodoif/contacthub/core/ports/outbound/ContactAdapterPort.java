package br.com.brunogodoif.contacthub.core.ports.outbound;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactAdapterPort {

    ContactDomain create(ContactCreateDomain contactToCreate);

    boolean update(ContactUpdateDomain contactToUpdate);

    List<ContactDomain> listAllContactsByProfessionalId(UUID professionalId);

    Optional<ContactDomain> getById(UUID id);

    boolean deleteById(UUID id);
}
