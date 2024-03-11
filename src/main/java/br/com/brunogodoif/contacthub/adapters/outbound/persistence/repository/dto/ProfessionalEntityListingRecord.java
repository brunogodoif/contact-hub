package br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfessionalEntityListingRecord(
        UUID id,
        String name,
        String role,
        LocalDateTime createdAt,
        Long totalContacts) {

}