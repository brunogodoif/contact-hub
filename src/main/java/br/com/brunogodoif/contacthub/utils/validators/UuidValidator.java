package br.com.brunogodoif.contacthub.utils.validators;

import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class UuidValidator {

    public static boolean isValidUUID(String uuidString) {
        try {
            if (StringUtils.isEmpty(uuidString))
                throw new IllegalArgumentException("UUID cannot be null or empty");
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            throw new InvalidParamUUIDTypeException("Value [" + uuidString + "] is not of type UUID");
        }
    }

}
