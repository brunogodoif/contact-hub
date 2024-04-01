package br.com.brunogodoif.contacthub.core.services

import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.ContactNotFoundException
import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.DatabaseAccessException
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity
import br.com.brunogodoif.contacthub.core.domain.ContactDomain
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort
import br.com.brunogodoif.contacthub.core.services.exceptions.ContactUpdatedException
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException
import br.com.brunogodoif.contacthub.core.services.exceptions.ProfessionalNotFoundException
import com.github.javafaker.Faker
import org.mockito.Mockito
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

import static java.time.LocalDateTime.now

class ContactPersistenceServiceSpock extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))
    @Shared
    def contactUUIDSetup = UUID.randomUUID()
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()
    @Shared
    ContactAdapterPort contactAdapter = Mockito.mock(ContactAdapterPort.class)
    @Shared
    ProfessionalAdapterPort professionalAdapterPort = Mockito.mock(ProfessionalAdapterPort.class)
    @Shared
    ContactPersistenceService contactPersistenceService = new ContactPersistenceService(contactAdapter, professionalAdapterPort)
    @Shared
    List<ContactDomain> listOfContactDomainSetup = new ArrayList<>()
    @Shared
    def createdAtSetup = now()
    @Shared
    def updatedAtSetup = now().plusMonths(1)

    def "Create contact successfully"() {
        setup:
        def email = fakerSetup.internet().emailAddress();
        def contactExpected = new ContactDomain(
                contactUUIDSetup,
                ContactTypeEnum.EMAIL,
                email,
                professionalUUIDSetup,
                true,
                createdAtSetup,
                updatedAtSetup
        )
        def contactToCreate = new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                email,
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.create(Mockito.any(ContactCreateDomain.class)))
                .thenReturn(contactExpected)

        when:
        def contactCreated = contactPersistenceService.create(contactToCreate)

        then:
        notThrown()
        contactCreated.getType() == contactExpected.getType()
        contactCreated.getContact() == contactExpected.getContact()
        contactCreated.getProfessionalId() == contactExpected.getProfessionalId()
        contactCreated.getActive() == contactExpected.getActive()
    }

    def "Should throws ProfessionalNotFoundException when try Create contact, professional not found"() {
        setup:
        def contactToCreate = new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty())

        when:
        contactPersistenceService.create(contactToCreate)

        then:
        thrown(ProfessionalNotFoundException)
    }

    def "Should throws DatabaseAccessException when try Create contact, database error on get professional"() {
        setup:
        def contactToCreate = new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactPersistenceService.create(contactToCreate)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try Create contact, database error"() {
        setup:
        def contactToCreate = new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.create(Mockito.any(ContactCreateDomain.class)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactPersistenceService.create(contactToCreate)

        then:
        thrown(DatabaseAccessException)
    }

    def "Update contact successfully"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        def contactUpdated = new ContactDomain(
                contactUUIDSetup,
                ContactTypeEnum.PHONE,
                fakerSetup.phoneNumber().phoneNumber(),
                professionalUUIDSetup,
                true,
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.update(Mockito.any(ContactUpdateDomain.class)))
                .thenReturn(true)

        Mockito.when(contactAdapter.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(contactUpdated))

        when:
        def updated = contactPersistenceService.update(contactToUpdate)

        then:
        notThrown()
        updated.getType() == contactUpdated.getType()
        updated.getContact() == contactUpdated.getContact()
        updated.getProfessionalId() == contactUpdated.getProfessionalId()
        updated.getActive() == contactUpdated.getActive()
    }

    def "Should throws ProfessionalNotFoundException when try Update contact, professional not found"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty())

        when:
        contactPersistenceService.update(contactToUpdate)

        then:
        thrown(ProfessionalNotFoundException)

    }

    def "Should throws DatabaseAccessException when try Update contact, database error on get professional"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactPersistenceService.update(contactToUpdate)

        then:
        thrown(DatabaseAccessException)

    }

    def "Should throws DatabaseAccessException when try Update contact, database error"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.update(Mockito.any(ContactUpdateDomain.class)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactPersistenceService.update(contactToUpdate)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws ContactUpdatedException when try Update contact, fail update"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.update(Mockito.any(ContactUpdateDomain.class)))
                .thenReturn(false);

        when:
        contactPersistenceService.update(contactToUpdate)

        then:
        thrown(ContactUpdatedException)
    }

    def "Should throws ContactNotFoundException after update contact"() {
        setup:
        def contactToUpdate = new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )

        Mockito.when(professionalAdapterPort.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.of(getProfessionalDomain()))

        Mockito.when(contactAdapter.update(Mockito.any(ContactUpdateDomain.class)))
                .thenReturn(true)

        Mockito.when(contactAdapter.getById(Mockito.any(UUID.class)))
                .thenReturn(Optional.empty())

        when:
        contactPersistenceService.update(contactToUpdate)

        then:
        thrown(ContactNotFoundException)
    }

    def "Delete contact by id"() {
        setup:
        Mockito.when(contactAdapter.deleteById(Mockito.any(UUID)))
                .thenReturn(true)

        when:
        def deleted = contactPersistenceService.deleteById(contactUUIDSetup.toString())

        then:
        notThrown()
        deleted
    }

    def "Should throws InvalidParamUUIDTypeException when delete contact by id"() {
        setup:
        def professionalId = ""

        when:
        contactPersistenceService.deleteById(professionalId)

        then:
        thrown(InvalidParamUUIDTypeException)
    }

    def "Should throws DatabaseAccessException when delete contact by id"() {
        setup:
        Mockito.when(contactAdapter.deleteById(Mockito.any(UUID)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactPersistenceService.deleteById(contactUUIDSetup.toString())

        then:
        thrown(DatabaseAccessException)
    }

    private ContactDomain getContactDomain() {
        return new ContactDomain(
                contactUUIDSetup,
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup,
                true,
                createdAtSetup,
                updatedAtSetup
        )
    }

    private ContactCreateDomain getContactCreateDomain() {
        return new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup.toString(),
                true,
        )
    }

    private ProfessionalDomain getProfessionalDomain() {

        Set<ContactDomain> contacts = new HashSet<>()
        contacts.add(new ContactDomain(
                UUID.randomUUID(),
                ContactTypeEnum.PHONE,
                fakerSetup.phoneNumber().phoneNumber(),
                professionalUUIDSetup,
                true,
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS))
        ))
        contacts.add(new ContactDomain(
                UUID.randomUUID(),
                ContactTypeEnum.EMAIL,
                fakerSetup.internet().emailAddress(),
                professionalUUIDSetup,
                true,
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS))
        ))

        return new ProfessionalDomain(
                professionalUUIDSetup,
                fakerSetup.name().name(),
                fakerSetup.job().position(),
                contacts,
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
                convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS))
        )
    }

    def getContacts(ProfessionalEntity professionalEntity, LocalDateTime createdAt) {
        Set<ContactEntity> contacts = new HashSet<>();

        contacts.add(createContact(professionalEntity, ContactTypeEnum.EMAIL, "john.doe@example.com", true, createdAt));
        contacts.add(createContact(professionalEntity, ContactTypeEnum.PHONE, "123-456-7890", true, createdAt));

        return contacts;
    }

    static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
    }
}
