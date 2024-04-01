package br.com.brunogodoif.contacthub.core.domain

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity
import br.com.brunogodoif.contacthub.core.domain.exceptions.ContactDomainException
import com.github.javafaker.Faker
import org.apache.commons.lang3.SerializationUtils
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.time.LocalDateTime.now

class ContactDomainSpec extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))

    @Shared
    def contactUUIDSetup = UUID.randomUUID()
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()
    @Shared
    def contactEntitySetup
    @Shared
    def professionalEntitySetup
    @Shared
    def createdAtSetup = now()
    @Shared
    def updatedAtSetup = now().plusMonths(1);


    def setup() {
        professionalEntitySetup = new ProfessionalEntity()
        professionalEntitySetup.setId(professionalUUIDSetup)

        contactEntitySetup = new ContactEntity()
        contactEntitySetup.setId(contactUUIDSetup)
        contactEntitySetup.setType(ContactTypeEnum.EMAIL)
        contactEntitySetup.setContact(fakerSetup.internet().emailAddress())
        contactEntitySetup.setProfessional(professionalEntitySetup)
        contactEntitySetup.setActive(true)
        contactEntitySetup.setCreatedAt(createdAtSetup)
        contactEntitySetup.setUpdatedAt(updatedAtSetup)

    }


    def "Create a valid ContactDomain"() {
        when:
        def email = fakerSetup.internet().emailAddress();

        ContactDomain contactDomain = new ContactDomain(contactUUIDSetup, ContactTypeEnum.EMAIL, email,
                professionalUUIDSetup, true, createdAtSetup, updatedAtSetup)

        then:
        noExceptionThrown()
        contactDomain.getId() == contactUUIDSetup
        contactDomain.getType() == ContactTypeEnum.EMAIL
        contactDomain.getContact() == email
        contactDomain.getProfessionalId() == professionalUUIDSetup
        contactDomain.getActive() == true
        contactDomain.getCreatedAt() == createdAtSetup
        contactDomain.getUpdatedAt() == updatedAtSetup
    }

    @Unroll
    def "Should throws ContactDomainException when [#field] is null or empty"() {
        when:

        new ContactDomain(contactId, type, contact, professionalId, true, createdAt, updatedAt)

        then:
        thrown(ContactDomainException)

        where:
        field            | contactId        | type                  | contact                              | professionalId        | createdAt      | updatedAt
        'id'             | null             | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | createdAtSetup | updatedAtSetup
        'type'           | contactUUIDSetup | null                  | fakerSetup.internet().emailAddress() | professionalUUIDSetup | createdAtSetup | updatedAtSetup
        'contact'        | contactUUIDSetup | ContactTypeEnum.EMAIL | null                                 | professionalUUIDSetup | createdAtSetup | updatedAtSetup
        'contact'        | contactUUIDSetup | ContactTypeEnum.EMAIL | ""                                   | professionalUUIDSetup | createdAtSetup | updatedAtSetup
        'professionalId' | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | null                  | createdAtSetup | updatedAtSetup
        'createdAt'      | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | null           | updatedAtSetup
        'updatedAt'      | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | createdAtSetup | null
    }

    def "Convert with successfully ContactEntity to ContatctDomain"() {
        when:
        ContactEntity contactEntity = SerializationUtils.clone(contactEntitySetup)
        ContactDomain contactDomain = ContactDomain.toDomain(contactEntity)

        then:
        noExceptionThrown()
        contactDomain.getId() == contactEntity.getId()
        contactDomain.getType() == contactEntity.getType()
        contactDomain.getContact() == contactEntity.getContact()
        contactDomain.getProfessionalId() == contactEntity.getProfessional().getId()
        contactDomain.getActive() == contactEntity.getActive()
    }

    @Unroll
    def "Should throws exception when [#field] is invalid when convert ContactEntity to ContatctDomain"() {
        when:
        ProfessionalEntity professionalEntity = SerializationUtils.clone(professionalEntitySetup)
        professionalEntity.setId(professionalId);

        ContactEntity contactEntity = SerializationUtils.clone(contactEntitySetup)
        contactEntity.setId(id)
        contactEntity.setType(type)
        contactEntity.setContact(contact)
        contactEntity.setProfessional(professionalEntity)
        contactEntity.setActive(active)
        contactEntity.setCreatedAt(createdAt)
        contactEntity.setUpdatedAt(updatedAt)

        ContactDomain.toDomain(contactEntity)

        then:
        thrown(ContactDomainException)

        where:
        field             | id               | type                  | contact                              | professionalId        | active | createdAt      | updatedAt
        'id'              | null             | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | true   | createdAtSetup | updatedAtSetup
        'professionalId'  | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | null                  | true   | createdAtSetup | updatedAtSetup
        'type (null)'     | contactUUIDSetup | null                  | fakerSetup.internet().emailAddress() | professionalUUIDSetup | true   | createdAtSetup | updatedAtSetup
        'contact (empty)' | contactUUIDSetup | ContactTypeEnum.EMAIL | ""                                   | professionalUUIDSetup | true   | createdAtSetup | updatedAtSetup
        'createdAt'       | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | true   | null           | updatedAtSetup
        'updatedAt'       | contactUUIDSetup | ContactTypeEnum.EMAIL | fakerSetup.internet().emailAddress() | professionalUUIDSetup | true   | createdAtSetup | null
    }
}
