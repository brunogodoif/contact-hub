package br.com.brunogodoif.contacthub.core.domain.request

import br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request.ContactPersistenceRequest
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException
import com.github.javafaker.Faker
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ContactUpdateDomainSpec extends Specification {


    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))
    @Shared
    def contactUUIDSetup = UUID.randomUUID()
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()

    def "Create a valid ContactUpdateDomain"() {
        when:
        String email = fakerSetup.internet().emailAddress()
        ContactUpdateDomain domain = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                email,
                professionalUUIDSetup.toString(),
                true
        )

        then:
        noExceptionThrown()
        domain.id == contactUUIDSetup
        domain.type == ContactTypeEnum.EMAIL
        domain.contact == email
        domain.professionalId == professionalUUIDSetup
        domain.active == true
    }

    @Unroll
    def "Should throws #expectedException when [#field] is null or empty"() {
        when:
        new ContactUpdateDomain(id, type, contact, professionalId, active)

        then:
        thrown(expectedException)

        where:
        field            | id                          | type                  | contact                              | professionalId                   | active | expectedException
        'id'             | null                        | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup.toString() | true   | InvalidParamUUIDTypeException
        'type'           | contactUUIDSetup.toString() | null                  | fakerSetup.internet().emailAddress() | professionalUUIDSetup.toString() | true   | ContactDomainException
        'contact'        | contactUUIDSetup.toString() | ContactTypeEnum.EMAIL | null                                 | professionalUUIDSetup.toString() | true   | ContactDomainException
        'contact'        | contactUUIDSetup.toString() | ContactTypeEnum.EMAIL | ""                                   | professionalUUIDSetup.toString() | true   | ContactDomainException
        'professionalId' | contactUUIDSetup.toString() | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | null                             | true   | InvalidParamUUIDTypeException
        'professionalId' | contactUUIDSetup.toString() | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | ""                               | true   | InvalidParamUUIDTypeException
    }

    def "Convert ContactPersistenceRequest to ContactUpdateDomain correctly"() {
        when:
        String email = fakerSetup.internet().emailAddress()
        ContactPersistenceRequest request = new ContactPersistenceRequest(
                ContactTypeEnum.EMAIL.toString(),
                email,
                professionalUUIDSetup.toString(),
                true
        )

        ContactUpdateDomain domain = ContactUpdateDomain.toDomain(request, contactUUIDSetup.toString())

        then:
        noExceptionThrown()
        domain.id == contactUUIDSetup
        domain.type == ContactTypeEnum.EMAIL
        domain.contact == email
        domain.professionalId == professionalUUIDSetup
        domain.active == true
    }

    @Unroll
    def "Should throws #expectedException when convert ContactPersistenceRequest to ContactUpdateDomain with [#field] invalid"() {
        when:
        ContactPersistenceRequest request = new ContactPersistenceRequest(
                type.toString(),
                contact,
                professionalId,
                active
        )

        ContactUpdateDomain.toDomain(request, id.toString())

        then:
        thrown(expectedException)

        where:
        field            | id                           | type                  | contact                              | professionalId               | active | expectedException
        'id'             | null                         | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | UUID.randomUUID().toString() | true   | InvalidParamUUIDTypeException
        'professionalId' | UUID.randomUUID().toString() | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | null                         | true   | InvalidParamUUIDTypeException
        'invalid type'   | UUID.randomUUID().toString() | null                  | fakerSetup.internet().emailAddress() | UUID.randomUUID().toString() | true   | IllegalArgumentException
    }
}
