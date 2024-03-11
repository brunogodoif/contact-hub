package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.response.ListingProfessionalsResponse;

import java.util.List;
import java.util.stream.Collectors;

public record ListingProfessionalsRecordResponse(List<ProfessionalListingRecordResponse> content, PaginationRecord pagination) {

    public static ListingProfessionalsRecordResponse fromDomain(ListingProfessionalsResponse listingProfessionalsResponse) {
        List<ProfessionalListingRecordResponse> listingProfessionalRecordsList = listingProfessionalsResponse.getContent()
                .stream()
                .map(ProfessionalListingRecordResponse::fromDomain)
                .collect(Collectors.toList());

        return new ListingProfessionalsRecordResponse(
                listingProfessionalRecordsList,
                PaginationRecord.fromDomain(listingProfessionalsResponse.getPagination())
        );
    }
}