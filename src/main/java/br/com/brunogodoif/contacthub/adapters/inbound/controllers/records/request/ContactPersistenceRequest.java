package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.request;

public record ContactPersistenceRequest(
        String type,
        String contact,
        String professionalId,
        Boolean active
) {
}