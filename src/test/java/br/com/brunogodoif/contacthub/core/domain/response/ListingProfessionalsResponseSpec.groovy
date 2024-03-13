package br.com.brunogodoif.contacthub.core.domain.response

import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse
import spock.lang.Specification

import java.time.LocalDateTime

class ListingProfessionalsResponseSpec extends Specification {

    def "Create a valid ListingProfessionalsResponse"() {
        given:
        def content = createProfessionalListingDomainList()
        def pagination = new PaginationResponse.PaginationBuilder(10, 2, 20, 1, true, false).build();

        when:
        def listingProfessionalsResponse = new ListingProfessionalsResponse(content, pagination)

        then:
        listingProfessionalsResponse.getContent() == content
        listingProfessionalsResponse.getPagination() == pagination
    }

    private List<ProfessionalListingDomain> createProfessionalListingDomainList() {
        List<ProfessionalListingDomain> content = []

        content << new ProfessionalListingDomain(
                UUID.randomUUID(),
                "John Doe",
                "Developer",
                3L,
                LocalDateTime.now()
        )

        return content
    }

}
