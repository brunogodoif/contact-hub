package br.com.brunogodoif.contacthub.core.ports.inbound;

import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse;

public interface ContactFindServicePort {
    ListingContactsResponse findAllByProfessionalId(String id);
}
