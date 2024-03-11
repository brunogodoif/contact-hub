package br.com.brunogodoif.contacthub.core.domain.response;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ListingContactsResponse {

    private final List<ContactDomain> content;
    private PaginationResponse pagination;

    public ListingContactsResponse(List<ContactDomain> content) {
        this.content = content;
    }

    public ListingContactsResponse(List<ContactDomain> content, PaginationResponse pagination) {
        this.content = content;
        this.pagination = pagination;
    }
}
