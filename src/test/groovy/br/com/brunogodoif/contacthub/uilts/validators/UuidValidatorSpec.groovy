package br.com.brunogodoif.contacthub.uilts.validators

import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator
import spock.lang.Specification

class UuidValidatorSpec extends Specification{
    def "Validates UUID correctly"() {
        given: "A valid UUID"
        String validUuid = "123e4567-e89b-12d3-a456-426614174000"

        when: "Checking if the UUID is valid"
        boolean isValid = UuidValidator.isValidUUID(validUuid)

        then: "The result should be true"
        isValid
        noExceptionThrown()
    }

    def "Invalidates an incorrect UUID"() {
        given: "An invalid UUID"
        String invalidUuid = "invalid-uuid"

        when: "Checking if the UUID is valid"
        UuidValidator.isValidUUID(invalidUuid)

        then: "An InvalidParamUUIDTypeException should be thrown"
        thrown(InvalidParamUUIDTypeException)
    }
}
