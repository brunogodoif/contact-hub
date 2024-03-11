package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

public record ProfessionalContactPersistenceRequest(
        String id,
        String type,
        String contact,
        Boolean active
) {
}