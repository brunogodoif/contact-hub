package br.com.brunogodoif.contacthub.core.services;

import br.com.brunogodoif.contacthub.core.domain.ContactDomain;
import br.com.brunogodoif.contacthub.core.domain.response.ListingContactsResponse;
import br.com.brunogodoif.contacthub.core.ports.inbound.ContactFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ContactAdapterPort;
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.UUID;

public class ContactFindService implements ContactFindServicePort {

    private final ContactAdapterPort contactAdapter;

    public ContactFindService(ContactAdapterPort contactAdapter) {
        this.contactAdapter = contactAdapter;
    }

    @Override
    public ListingContactsResponse findAllByProfessionalId(String id) {

        if (StringUtils.isEmpty(id))
            throw new InvalidParamUUIDTypeException("Param [id] not found");

        UuidValidator.isValidUUID(id);

        List<ContactDomain> contactsPaginate = contactAdapter.listAllContactsByProfessionalId(UUID.fromString(id));
        return new ListingContactsResponse(contactsPaginate);
    }

}
