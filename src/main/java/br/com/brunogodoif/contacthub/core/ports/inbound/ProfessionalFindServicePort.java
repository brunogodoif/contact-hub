package br.com.brunogodoif.contacthub.core.ports.inbound;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalSearchListing;
import br.com.brunogodoif.contacthub.core.domain.response.ListingProfessionalsResponse;

public interface ProfessionalFindServicePort {

    ListingProfessionalsResponse findAllWithPaginate(ProfessionalSearchListing professionalSearchListing, PaginationRequest paginationRequest);

    ProfessionalDomain findById(String id);

}
