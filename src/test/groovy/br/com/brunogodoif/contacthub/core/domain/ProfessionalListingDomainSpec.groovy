package br.com.brunogodoif.contacthub.core.domain


import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto.ProfessionalEntityListingRecord
import br.com.brunogodoif.contacthub.core.domain.exceptions.ProfessionalDomainException
import com.github.javafaker.Faker
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static java.time.LocalDateTime.now

class ProfessionalListingDomainSpec extends Specification {

    @Shared
    def fakerSetup = new Faker(new Locale("pt-BR"))
    @Shared
    def professionalUUIDSetup = UUID.randomUUID()
    @Shared
    def createdAtSetup = now()

    def "Create a valid ProfessionalListingDomain"() {
        when:
        String name = fakerSetup.name().name();
        String role = fakerSetup.job().position();
        int totalContacts = fakerSetup.number().numberBetween(1, 50);
        ProfessionalListingDomain professionalListingDomain = new ProfessionalListingDomain(
                professionalUUIDSetup, name, role, totalContacts, createdAtSetup
        )

        then:
        noExceptionThrown()
        professionalListingDomain.getId() == professionalUUIDSetup
        professionalListingDomain.getName() == name
        professionalListingDomain.getRole() == role
        professionalListingDomain.getTotalContacts() == totalContacts
        professionalListingDomain.getCreatedAt() == createdAtSetup

    }

    def "Should throws ProfessionalDomainException when [#field] is null or empty"() {
        when:
        new ProfessionalListingDomain(professionalUUIDSetup, name, role, 10, createdAtSetup)

        then:
        thrown(ProfessionalDomainException)

        where:
        field  | name                     | role
        "name" | null                     | fakerSetup.job().position()
        "name" | ""                       | fakerSetup.job().position()
        "role" | fakerSetup.name().name() | null
        "role" | fakerSetup.name().name() | ""

    }

    def "Convert with successfully ProfessionalEntityListingRecord to ProfessionalListingDomain"() {
        when:
        ProfessionalEntityListingRecord professionalEntityListingRecord = new ProfessionalEntityListingRecord(
                professionalUUIDSetup,
                fakerSetup.name().name(),
                fakerSetup.job().position(),
                createdAtSetup.plusMonths(1),
                fakerSetup.number().numberBetween(1, 50)
        )

        ProfessionalListingDomain professionalListingDomain = ProfessionalListingDomain::toDomain(professionalEntityListingRecord)

        then:
        noExceptionThrown()
        professionalListingDomain.getId() == professionalEntityListingRecord.id();
        professionalListingDomain.getName() == professionalEntityListingRecord.name();
        professionalListingDomain.getRole() == professionalEntityListingRecord.role();
        professionalListingDomain.getCreatedAt() == professionalEntityListingRecord.createdAt();
        professionalListingDomain.getTotalContacts() == professionalEntityListingRecord.totalContacts();

    }

    @Unroll
    def "Should throws exception when [#field] is invalid when convert ProfessionalEntityListingRecord to ProfessionalListingDomain"() {
        when:
        ProfessionalEntityListingRecord professionalEntityListingRecord = new ProfessionalEntityListingRecord(
                professionalUUIDSetup,
                name,
                role,
                createdAtSetup.plusMonths(1),
                fakerSetup.number().numberBetween(1, 50)
        )

        ProfessionalListingDomain::toDomain(professionalEntityListingRecord)
        then:
        thrown(ProfessionalDomainException)

        where:
        field  | name                     | role
        "name" | null                     | fakerSetup.job().position()
        "name" | ""                       | fakerSetup.job().position()
        "role" | fakerSetup.name().name() | null
        "role" | fakerSetup.name().name() | ""
    }


}
