package br.com.brunogodoif.contacthub.adapters.outbound

import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.ContactNotFoundException
import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.DatabaseAccessException
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.ContactRepository
import br.com.brunogodoif.contacthub.core.domain.ContactDomain
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.request.ContactCreateDomain
import br.com.brunogodoif.contacthub.core.domain.request.ContactUpdateDomain
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort
import com.github.javafaker.Faker
import org.mockito.Mockito
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.data.domain.Sort
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

import static java.time.LocalDateTime.now

class ContactAdapterPortImplSpec extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))
    @Shared
    ContactRepository contactRepositoryMock = Mockito.mock(ContactRepository.class)
    @Shared
    ContactAdapterPort contactAdapterPort = new ContactAdapterPortImpl(contactRepositoryMock)
    @Shared
    def contactUUIDSetup = UUID.randomUUID()
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()
    @Shared
    ContactEntity contactEntitySetup
    @Shared
    ProfessionalEntity professionalEntitySetup
    @Shared
    List<ContactEntity> listOfContactEntitiesSetup = new ArrayList<>()
    @Shared
    def createdAtSetup = now()
    @Shared
    def updatedAtSetup = now().plusMonths(1)
    @Shared
    def contactPhoneDefault = fakerSetup.phoneNumber().phoneNumber()

    def setup() {
        setupProfessionalEntity()
        setupContactEntity(professionalEntitySetup)
        setupListOfContactEntity()
    }


    def "Create and save contact"() {
        setup:
        Mockito.when(contactRepositoryMock.save(Mockito.any(ContactEntity)))
                .thenReturn(contactEntitySetup)

        def contactToCreate = createContactDomain()

        when:
        def createdContact = contactAdapterPort.create(contactToCreate)

        then:
        notThrown()
        createdContact.id == contactEntitySetup.getId()
        createdContact.type == contactEntitySetup.getType()
        createdContact.contact == contactEntitySetup.getContact()
        createdContact.active == contactEntitySetup.getActive()
    }

    def "Should throws DatabaseAccessException create contact"() {
        setup:
        Mockito.when(contactRepositoryMock.save(Mockito.any(ContactEntity)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        def contactToCreate = createContactDomain()

        when:
        contactAdapterPort.create(contactToCreate)

        then:
        thrown(DatabaseAccessException)

    }

    def "Should update contact by id from ContactUpdateDomain"() {
        setup:
        def contactToUpdate = createContactUpdateDomain(ContactTypeEnum.PHONE, contactPhoneDefault)

        def contatcUpdated = new ContactEntity(
                id: contactUUIDSetup,
                type: ContactTypeEnum.PHONE,
                contact: contactPhoneDefault,
                professional: professionalEntitySetup,
                active: true,
                createdAt: now(),
                updatedAt: now()
        )

        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.findById(Mockito.any(UUID)))
                .thenReturn(Optional.of(contactEntitySetup))

        Mockito.when(contactRepositoryMock.save(Mockito.any(ContactEntity.class)))
                .thenReturn(contatcUpdated)

        when:
        def isUpdated = contactAdapterPort.update(contactToUpdate)

        then:
        isUpdated
    }

    def "Should throws ContactNotFoundException when try update contact, contact not exists"() {
        setup:
        def number = fakerSetup.phoneNumber().phoneNumber();
        def contactToUpdate = createContactUpdateDomain(ContactTypeEnum.PHONE, contactPhoneDefault)

        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(false)

        when:
        contactAdapterPort.update(contactToUpdate)

        then:
        thrown(ContactNotFoundException)
    }

    def "Should throws DatabaseAccessException when try update contact - existsById error"() {
        setup:
        def contactToUpdate = createContactUpdateDomain(ContactTypeEnum.PHONE, contactPhoneDefault)

        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        when:
        contactAdapterPort.update(contactToUpdate)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try update contact, error save"() {
        setup:
        def contactToUpdate = createContactUpdateDomain(ContactTypeEnum.PHONE, contactPhoneDefault)

        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.findById(Mockito.any(UUID)))
                .thenReturn(Optional.of(contactEntitySetup))

        Mockito.when(contactRepositoryMock.save(Mockito.any(ContactEntity.class)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        when:
        contactAdapterPort.update(contactToUpdate)

        then:
        thrown(DatabaseAccessException)
    }

    def "Get a list of contacts by professional id"() {
        setup:
        Mockito.when(contactRepositoryMock.findAllByProfessionalId(Mockito.any(UUID), Mockito.any(Sort)))
                .thenReturn(listOfContactEntitiesSetup)
        when:
        def contacts = contactAdapterPort.listAllContactsByProfessionalId(professionalUUIDSetup)

        then:
        contacts.size() == listOfContactEntitiesSetup.size()
        contacts.every { contact ->
            listOfContactEntitiesSetup.any { entity ->
                entity.id == contact.getId() &&
                        entity.type == contact.getType()
                entity.contact == contact.getContact()
                entity.active == contact.getActive()
                entity.professional.id == contact.getProfessionalId()
                entity.createdAt == contact.getCreatedAt()
                entity.updatedAt == contact.getUpdatedAt()
            }
        }
    }

    def "Should return a empty list of contacts by professional id"() {
        setup:
        Mockito.when(contactRepositoryMock.findAllByProfessionalId(Mockito.any(UUID), Mockito.any(Sort)))
                .thenReturn(Collections.emptyList())
        when:
        def contacts = contactAdapterPort.listAllContactsByProfessionalId(professionalUUIDSetup)
        then:
        notThrown()
        contacts.isEmpty()
    }

    def "Should throws DatabaseAccessException when try list all contacts by professional id"() {
        setup:
        Mockito.when(contactRepositoryMock.findAllByProfessionalId(Mockito.any(UUID), Mockito.any(Sort)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))
        when:
        contactAdapterPort.listAllContactsByProfessionalId(professionalUUIDSetup)
        then:
        thrown(DatabaseAccessException)
    }

    def "Get contact by id"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.findById(Mockito.any(UUID)))
                .thenReturn(Optional.of(contactEntitySetup))

        when:
        def contactEntityResponse = contactAdapterPort.getById(contactUUIDSetup)

        then:
        notThrown()
        contactEntityResponse.get() == ContactDomain.toDomain(contactEntitySetup)
    }

    def "Try find contact by id, but contact not exists"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(false)

        when:
        contactAdapterPort.getById(contactUUIDSetup)

        then:
        thrown(ContactNotFoundException)
    }

    def "Should throws DatabaseAccessException when try find contact by id - existsById error"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenThrow(DatabaseAccessException)

        when:
        contactAdapterPort.getById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try find contact by id - findById error"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.findById(Mockito.any(UUID)))
                .thenThrow(DatabaseAccessException)

        when:
        contactAdapterPort.getById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    def "Delete contact by id"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.doNothing().when(contactRepositoryMock).deleteById(Mockito.any(UUID))

        when:
        contactAdapterPort.deleteById(contactUUIDSetup)

        then:
        notThrown()
    }

    def "Should delete contact by id, but contact not exists"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(false)

        when:
        contactAdapterPort.deleteById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try delete contact by id - existsById error"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        when:
        contactAdapterPort.deleteById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try delete contact by id - findById error"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        when:
        contactAdapterPort.deleteById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    def "Should throws DatabaseAccessException when try delete contact by id"() {
        setup:
        Mockito.when(contactRepositoryMock.existsById(Mockito.any(UUID)))
                .thenReturn(true)

        Mockito.when(contactRepositoryMock.deleteById(Mockito.any(UUID)))
                .thenThrow(new DataAccessResourceFailureException("Error connect database"))

        when:
        contactAdapterPort.deleteById(contactUUIDSetup)

        then:
        thrown(DatabaseAccessException)
    }

    private void setupContactEntity(ProfessionalEntity professionalEntitySetup) {
        contactEntitySetup = new ContactEntity()
        contactEntitySetup.setId(contactUUIDSetup)
        contactEntitySetup.setType(ContactTypeEnum.EMAIL)
        contactEntitySetup.setContact(fakerSetup.internet().emailAddress())
        contactEntitySetup.setProfessional(professionalEntitySetup)
        contactEntitySetup.setActive(true)
        contactEntitySetup.setCreatedAt(createdAtSetup)
        contactEntitySetup.setUpdatedAt(updatedAtSetup)
    }

    private ProfessionalEntity setupProfessionalEntity() {
        professionalEntitySetup = new ProfessionalEntity()
        professionalEntitySetup.setId(professionalUUIDSetup)
        professionalEntitySetup
    }

    private void setupListOfContactEntity() {
        def contactEntities = generateContactEntities(5)
        listOfContactEntitiesSetup.addAll(contactEntities)
    }

    private ContactCreateDomain createContactDomain() {
        new ContactCreateDomain(
                ContactTypeEnum.EMAIL,
                contactEntitySetup.getContact(),
                contactEntitySetup.getProfessional().getId().toString(),
                contactEntitySetup.getActive()
        )
    }
    private List<ContactEntity> generateContactEntities(int numberOfEntities) {
        List<ContactEntity> entities = new ArrayList<>()
        Faker faker = new Faker()

        ProfessionalEntity professional = new ProfessionalEntity()
        professional.setId(professionalUUIDSetup)

        for (int i = 0; i < numberOfEntities; i++) {
            ContactEntity entity = new ContactEntity()
            entity.setId(UUID.randomUUID())
            entity.setType(ContactTypeEnum.values()[faker.number().numberBetween(0, ContactTypeEnum.values().length)])
            entity.setContact(faker.phoneNumber().phoneNumber())
            entity.setActive(faker.random().nextBoolean())
            entity.setCreatedAt(convertToLocalDateTime(faker.date().past(365, TimeUnit.DAYS)))
            entity.setUpdatedAt(convertToLocalDateTime(faker.date().past(365, TimeUnit.DAYS)))
            entity.setProfessional(professional)
            entities.add(entity)
        }

        return entities
    }

    static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
    }

    private ContactUpdateDomain createContactUpdateDomain(ContactTypeEnum contactTypeEnum, String contact) {
        new ContactUpdateDomain(
                contactUUIDSetup.toString(),
                contactTypeEnum,
                contact,
                professionalUUIDSetup.toString(),
                true
        )
    }
}
