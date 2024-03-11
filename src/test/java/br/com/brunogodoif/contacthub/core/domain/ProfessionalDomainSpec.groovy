package br.com.brunogodoif.contacthub.core.domain

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException
import com.github.javafaker.Faker
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

class ProfessionalDomainSpec extends Specification {

    @Shared
    def faker = new Faker(Locale.US)
    @Shared
    def professionalId = UUID.randomUUID()
    @Shared
    def createdAtSetup = LocalDateTime.now()
    @Shared
    def updatedAtSetup = LocalDateTime.now().plusMonths(1)

    def "Create a valid ProfessionalDomain"() {
        when:
        String name = faker.name().name()
        String role = faker.job().position()
        Set<ContactDomain> contacts = Collections.emptySet()

        ProfessionalDomain professionalDomain = new ProfessionalDomain(professionalId, name, role, contacts, createdAtSetup, createdAtSetup)

        then:
        noExceptionThrown()
        professionalDomain.getId() == professionalId
        professionalDomain.getName() == name
        professionalDomain.getRole() == role
        professionalDomain.getContacts().isEmpty()
        professionalDomain.getCreatedAt() == createdAtSetup
        professionalDomain.getUpdatedAt() == createdAtSetup
    }

    @Unroll
    def "Should throw ProfessionalDomainException when [#field] is null or empty"() {
        when:
        new ProfessionalDomain(professionalId, name, role, Collections.emptySet(), createdAt, updated)

        then:
        thrown(ProfessionalDomainException)

        where:
        field       | name                | role                    | createdAt      | updated
        "name"      | null                | faker.job().position()  | createdAtSetup | updatedAtSetup
        "name"      | ""                  | faker.job().position() || createdAtSetup | updatedAtSetup
        "role"      | faker.name().name() | null                    | createdAtSetup | updatedAtSetup
        "role"      | faker.name().name() | ""                      | createdAtSetup | updatedAtSetup
        "createdAt" | faker.name().name() | ""                      | null           | updatedAtSetup
        "updatedAt" | faker.name().name() | ""                      | createdAtSetup | null
    }

    def "Convert with successfully ProfessionalEntity to domain ProfessionalDomain"() {
        when:
        ProfessionalEntity professionalEntity = getProfessionalEntity()
        ProfessionalDomain professionalDomain = ProfessionalDomain.toDomain(professionalEntity)

        then:
        noExceptionThrown()
        professionalDomain.getId() == professionalEntity.getId()
        professionalDomain.getName() == professionalEntity.getName()
        professionalDomain.getRole() == professionalEntity.getRole()
        professionalDomain.getCreatedAt() == professionalEntity.getCreatedAt()
        professionalDomain.getUpdatedAt() == professionalEntity.getUpdatedAt()
        professionalDomain.getContacts().size() == 2
    }

    def "Convert with successfully ProfessionalEntity to domain ProfessionalDomain with contacts when include Contacts"() {
        when:
        ProfessionalEntity professionalEntity = getProfessionalEntity()
        ProfessionalDomain professionalDomain = ProfessionalDomain.toDomain(professionalEntity, true)

        then:
        noExceptionThrown()
        professionalDomain.getId() == professionalEntity.getId()
        professionalDomain.getName() == professionalEntity.getName()
        professionalDomain.getRole() == professionalEntity.getRole()
        professionalDomain.getCreatedAt() == professionalEntity.getCreatedAt()
        professionalDomain.getUpdatedAt() == professionalEntity.getUpdatedAt()
        professionalDomain.getContacts().size() == 2
    }

    def "Convert with successfully ProfessionalEntity to domain ProfessionalDomain with contacts when not include Contacts"() {
        when:
        ProfessionalEntity professionalEntity = getProfessionalEntity()
        professionalEntity.setContacts(Collections.emptySet())
        ProfessionalDomain professionalDomain = ProfessionalDomain.toDomain(professionalEntity, false)

        then:
        noExceptionThrown()
        professionalDomain.getId() == professionalEntity.getId()
        professionalDomain.getName() == professionalEntity.getName()
        professionalDomain.getRole() == professionalEntity.getRole()
        professionalDomain.getCreatedAt() == professionalEntity.getCreatedAt()
        professionalDomain.getUpdatedAt() == professionalEntity.getUpdatedAt()
        professionalDomain.getContacts().size() == 0
    }

    def getProfessionalEntity() {
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        String role = "Developer";
        Date birthDate = new Date();
        LocalDateTime now = LocalDateTime.now();

        ProfessionalEntity professionalEntity = new ProfessionalEntity();
        professionalEntity.setId(id);
        professionalEntity.setName(name);
        professionalEntity.setRole(role);
        professionalEntity.setBirthDate(birthDate);
        professionalEntity.setCreatedAt(now);
        professionalEntity.setUpdatedAt(now);
        professionalEntity.setContacts(getContacts(professionalEntity, now));

        return professionalEntity;
    }

    def getContacts(ProfessionalEntity professionalEntity, LocalDateTime createdAt) {
        Set<ContactEntity> contacts = new HashSet<>();

        contacts.add(createContact(professionalEntity, ContactTypeEnum.EMAIL, "john.doe@example.com", true, createdAt));
        contacts.add(createContact(professionalEntity, ContactTypeEnum.PHONE, "123-456-7890", true, createdAt));

        return contacts;
    }

    def createContact(ProfessionalEntity professionalEntity, ContactTypeEnum type, String contact, boolean active, LocalDateTime createdAt) {
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setId(UUID.randomUUID());
        contactEntity.setType(type);
        contactEntity.setContact(contact);
        contactEntity.setProfessional(professionalEntity);
        contactEntity.setActive(active);
        contactEntity.setCreatedAt(createdAt);
        contactEntity.setUpdatedAt(createdAt);
        return contactEntity;
    }
}
