package br.com.brunogodoif.contacthub.core.domain.response

import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationBuilder
import spock.lang.Specification

import java.time.LocalDateTime

class ListingProfessionalsResponseSpec extends Specification {

    def "Testando criação de ListingProfessionalsResponse com conteúdo e paginação"() {
        given:
        def content = createProfessionalListingDomainList()
        def pagination = new PaginationBuilder(10, 2, 20, 1, true, false).build();


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
