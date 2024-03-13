package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Response representing a listing of contacts")
public record ListingContactsRecord(
        @Schema(description = "List of contact records")
        List<ContactRecord> content
) {

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