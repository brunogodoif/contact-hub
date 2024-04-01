package br.com.brunogodoif.contacthub.core.services

import br.com.brunogodoif.contacthub.adapters.outbound.exceptions.DatabaseAccessException
import br.com.brunogodoif.contacthub.core.domain.ContactDomain
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum
import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException
import com.github.javafaker.Faker
import org.mockito.Mockito
import org.springframework.dao.DataAccessResourceFailureException
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

class ContactFindServiceSpec extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()
    @Shared
    ContactAdapterPort contactAdapter = Mockito.mock(ContactAdapterPort.class)
    @Shared
    ContactFindService contactFindService = new ContactFindService(contactAdapter)
    @Shared
    List<ContactDomain> listOfContactDomainSetup = new ArrayList<>()

    def setup() {
        setupListOfContactsDomain()
    }

    def "Find all contacts associated a professional"() {
        setup:

        Mockito.when(contactAdapter.listAllContactsByProfessionalId(Mockito.any(UUID)))
                .thenReturn(listOfContactDomainSetup)

        when:
        ListingContactsResponse response = contactFindService.findAllByProfessionalId(professionalUUIDSetup.toString())

        then:
        notThrown()
        response.getContent().size() == listOfContactDomainSetup.size()
        response.getContent().every { contact ->
            listOfContactDomainSetup.any { contactDomain ->
                contactDomain.id == contact.getId() &&
                        contactDomain.type == contact.getType()
                contactDomain.contact == contact.getContact()
                contactDomain.active == contact.getActive()
                contactDomain.professionalId == contact.getProfessionalId()
                contactDomain.createdAt == contact.getCreatedAt()
                contactDomain.updatedAt == contact.getUpdatedAt()
            }
        }
    }

    def "Return empty List Contacts Response, when a professional has no associated contacts"() {
        setup:

        Mockito.when(contactAdapter.listAllContactsByProfessionalId(Mockito.any(UUID)))
                .thenReturn(Collections.emptyList())

        when:
        ListingContactsResponse response = contactFindService.findAllByProfessionalId(professionalUUIDSetup.toString())

        then:
        notThrown()
        response.getContent().size() == 0
    }

    def "Should throws InvalidParamUUIDTypeException when find all contacts associated a professional"() {
        setup:
        def professionalId = ""

        when:
        contactFindService.findAllByProfessionalId(professionalId)

        then:
        thrown(InvalidParamUUIDTypeException)
    }

    def "Should throws DatabaseAccessException when find all contacts associated a professional"() {
        setup:

        Mockito.when(contactAdapter.listAllContactsByProfessionalId(Mockito.any(UUID)))
                .thenThrow(new DatabaseAccessException())

        when:
        contactFindService.findAllByProfessionalId(professionalUUIDSetup.toString())

        then:
        thrown(DatabaseAccessException)
    }

    private void setupListOfContactsDomain() {
        def contactEntities = generateContactDomains(5)
        listOfContactDomainSetup.addAll(contactEntities)
    }

    List<ContactDomain> generateContactDomains(int numberOfDomains) {
        List<ContactDomain> domains = new ArrayList<>()
        for (int i = 0; i < numberOfDomains; i++) {
            ContactDomain domain = new ContactDomain(
                    UUID.randomUUID(),
                    ContactTypeEnum.values()[fakerSetup.number().numberBetween(0, ContactTypeEnum.values().length)],
                    fakerSetup.phoneNumber().phoneNumber(),
                    professionalUUIDSetup,
                    fakerSetup.random().nextBoolean(),
                    convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS)),
                    convertToLocalDateTime(fakerSetup.date().past(365, TimeUnit.DAYS))
            )
            domains.add(domain)
        }

        return domains
    }

    static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
    }


}
