package br.com.brunogodoif.contacthub.core.domain.response

import br.com.brunogodoif.contacthub.core.domain.ContactDomain
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse
import spock.lang.Specification

import java.time.LocalDateTime

class ListingContactsResponseSpec extends Specification {

    def "Testando criação de ListingContactsResponse com conteúdo e paginação"() {
        given:
        def content = createContactDomainList()
        def pagination = new PaginationResponse(10, 2, 20, 1, 2, 1, true, false)

        when:
        def listingContactsResponse = new ListingContactsResponse(content, pagination)

        then:
        listingContactsResponse.content == content
        listingContactsResponse.pagination == pagination
    }

    def "Testando criação de ListingContactsResponse sem paginação"() {
        given:
        def content = createContactDomainList()

        when:
        def listingContactsResponse = new ListingContactsResponse(content)

        then:
        listingContactsResponse.content == content
        listingContactsResponse.pagination == null
    }

    private List<ContactDomain> createContactDomainList() {
        List<ContactDomain> content = []

        content << new ContactDomain(
                UUID.randomUUID(),
                ContactTypeEnum.EMAIL,
                "test@example.com",
                UUID.randomUUID(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        )

        content << new ContactDomain(
                UUID.randomUUID(),
                ContactTypeEnum.PHONE,
                "123-456-7890",
                UUID.randomUUID(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        )

        return content
    }

}
