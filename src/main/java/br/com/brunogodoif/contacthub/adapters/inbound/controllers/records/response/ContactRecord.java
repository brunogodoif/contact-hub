package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ContactRecord(
        String id,
        ContactTypeEnum type,
        String contact,
        Boolean active,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime updatedAt
) {

        public static ContactRecord fromDomain(ContactDomain contactDomain) {
                return new ContactRecord(
                        contactDomain.getId().toString(),
                        contactDomain.getType(),
                        contactDomain.getContact(),
                        contactDomain.getActive(),
                        contactDomain.getCreatedAt(),
                        contactDomain.getUpdatedAt()
                );
        }

}