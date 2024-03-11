package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record ProfessionalResponse(
        UUID id,
        String name,
        String role,
        Date birthDate,
        Set<ContactRecord> contacts,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime updatedAt
) {}