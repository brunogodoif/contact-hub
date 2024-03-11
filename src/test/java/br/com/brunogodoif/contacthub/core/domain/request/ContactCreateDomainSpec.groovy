package br.com.brunogodoif.contacthub.core.domain.request

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ContactPersistenceRequest
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException
import com.github.javafaker.Faker
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ContactCreateDomainSpec extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))

    @Shared
    def professionalUUIDSetup = UUID.randomUUID()

    def "Create a valid ContactCreateDomain"() {
        when:
        String email = fakerSetup.internet().emailAddress()
        ContactCreateDomain domain = new ContactCreateDomain(
                ContactTypeEnum.EMAIL, email, professionalUUIDSetup.toString(), true
        )

        then:
        noExceptionThrown()
        domain.type == ContactTypeEnum.EMAIL
        domain.contact == email
        domain.professionalId == professionalUUIDSetup
        domain.active == true
    }

    @Unroll
    def "Should throws #expectedException when create a ContactDomain with [#field] is null or empty"() {
        when:
        new ContactCreateDomain(type, contact, professionalId, active)

        then:
        thrown(expectedException)

        where:
        field            | type                  | contact                              | professionalId                   | active | expectedException
        'type'           | null                  | fakerSetup.internet().emailAddress() | professionalUUIDSetup.toString() | true   | ContactDomainException
        'contact'        | ContactTypeEnum.EMAIL | null                                 | professionalUUIDSetup.toString() | true   | ContactDomainException
        'contact'        | ContactTypeEnum.EMAIL | ""                                   | professionalUUIDSetup.toString() | true   | ContactDomainException
        'professionalId' | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | null                             | true   | InvalidParamUUIDTypeException
        'professionalId' | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | ""                               | true   | InvalidParamUUIDTypeException
        'contact'        | ContactTypeEnum.EMAIL | null                                 | professionalUUIDSetup.toString() | true   | ContactDomainException
        'contact'        | ContactTypeEnum.EMAIL | ""                                   | professionalUUIDSetup.toString() | true   | ContactDomainException
    }

    @Unroll
    def "Convert ContactPersistenceRequest to ContactCreateDomain correctly"() {
        when:
        String email = fakerSetup.internet().emailAddress()
        ContactPersistenceRequest request = new ContactPersistenceRequest(
                ContactTypeEnum.EMAIL.toString(),
                email,
                professionalUUIDSetup.toString(),
                true
        )

        ContactCreateDomain domain = ContactCreateDomain.toDomain(request)

        then:
        noExceptionThrown()
        domain.type == ContactTypeEnum.EMAIL
        domain.contact == email
        domain.professionalId == professionalUUIDSetup
        domain.active == true

    }

    @Unroll
    def "Should throws #expectedException when convert ContactPersistenceRequest to ContactCreateDomain with [#field] invalid"() {
        when:
        ContactPersistenceRequest request = new ContactPersistenceRequest(
                type.toString(),
                contact,
                professionalId,
                active
        )

        ContactCreateDomain.toDomain(request)

        then:
        thrown(expectedException)

        where:
        field     | type                  | contact                              | professionalId                   | active | expectedException
        'type'    | null                  | fakerSetup.internet().emailAddress() | professionalUUIDSetup.toString() | true   | IllegalArgumentException
        'contact' | ContactTypeEnum.EMAIL | null                                 | professionalUUIDSetup.toString() | true   | ContactDomainException
    }

}
