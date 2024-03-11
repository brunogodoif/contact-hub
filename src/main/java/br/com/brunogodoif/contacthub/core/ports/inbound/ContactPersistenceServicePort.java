package br.com.brunogodoif.contacthub.core.ports.inbound;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain;

public interface ContactPersistenceServicePort {

    ContactDomain create(ContactCreateDomain contactCreateDomain);
    ContactDomain update(ContactUpdateDomain contactUpdateDomain);
    boolean deleteById(String id);
}
