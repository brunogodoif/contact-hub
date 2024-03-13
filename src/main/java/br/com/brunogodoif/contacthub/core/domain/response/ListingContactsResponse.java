package br.com.brunogodoif.contacthub.core.domain.response;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import lombok.Getter;

import java.util.List;

@Getter
public class ListingContactsResponse {

    private final List<ContactDomain> content;
    public ListingContactsResponse(List<ContactDomain> content) {
        this.content = content;
    }

}
