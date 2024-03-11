package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse;

import java.util.List;
import java.util.stream.Collectors;

public record ListingContactsRecord(List<ContactRecord> content) {

    public static ListingContactsRecord fromDomain(ListingContactsResponse listingContactsResponse) {
        List<ContactRecord> contactRecords = listingContactsResponse.getContent()
                .stream()
                .map(ContactRecord::fromDomain)
                .collect(Collectors.toList());

        return new ListingContactsRecord(
                contactRecords
        );
    }
}