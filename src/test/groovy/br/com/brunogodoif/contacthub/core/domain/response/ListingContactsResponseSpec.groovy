package br.com.brunogodoif.contacthub.core.domain.response

import br.com.brunogodoif.contacthub.core.domain.ContactDomain
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import spock.lang.Specification

import java.time.LocalDateTime

class ListingContactsResponseSpec extends Specification {

    def "Create a valid ListingContactsResponse with "() {
        given:
        def content = createContactDomainList()

        when:
        def listingContactsResponse = new ListingContactsResponse(content)

        then:
        listingContactsResponse.content == content
    }

    def "Create a valid ListingContactsResponse with content empty "() {
        when:
        def listingContactsResponse = new ListingContactsResponse(Collections.emptyList())

        then:
        listingContactsResponse.content == Collections.emptyList()
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
