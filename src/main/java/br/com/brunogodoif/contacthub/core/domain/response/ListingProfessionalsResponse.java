package br.com.brunogodoif.contacthub.core.domain.response;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ListingProfessionalsResponse {

    private final List<ProfessionalListingDomain> content;
    private final PaginationResponse pagination;

    public ListingProfessionalsResponse(List<ProfessionalListingDomain> content, PaginationResponse pagination) {
        this.content = content;
        this.pagination = pagination;
    }
}
