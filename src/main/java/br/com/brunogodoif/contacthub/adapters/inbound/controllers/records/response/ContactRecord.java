package br.com.brunogodoif.contacthub.adapters.inbound.controllers.records.response;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.ContactTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response representing a contact")
public record ContactRecord(
        @Schema(description = "Contact ID")
        String id,
        @Schema(description = "Contact type")
        ContactTypeEnum type,
        @Schema(description = "Contact information")
        String contact,
        @Schema(description = "Flag indicating if the contact is active")
        Boolean active,
        @Schema(description = "Creation timestamp")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt,
        @Schema(description = "Update timestamp")
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