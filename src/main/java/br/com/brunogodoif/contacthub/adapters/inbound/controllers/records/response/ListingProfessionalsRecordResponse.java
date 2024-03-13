package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.response.ListingProfessionalsResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "Response representing a listing of professionals")
public record ListingProfessionalsRecordResponse( @Schema(description = "List of professional records")
                                                  List<ProfessionalListingRecordResponse> content,
                                                  @Schema(description = "Pagination information")
                                                  PaginationRecord pagination) {

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