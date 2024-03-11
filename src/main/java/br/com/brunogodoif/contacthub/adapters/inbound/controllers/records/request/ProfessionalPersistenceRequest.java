package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

import java.util.Date;
import java.util.List;

public record ProfessionalPersistenceRequest(
        String name,
        String role,
        Date birthDate,
        List<ProfessionalContactPersistenceRequest> contacts
) {
}