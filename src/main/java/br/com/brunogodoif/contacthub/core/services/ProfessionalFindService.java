package br.com.brunogodoif.contacthub.core.services;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationResponse;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalSearchListing;
import br.com.brunogodoif.contacthub.core.domain.response.ListingProfessionalsResponse;
import br.com.brunogodoif.contacthub.core.ports.inbound.ProfessionalFindServicePort;
import br.com.brunogodoif.contacthub.core.ports.outbound.ProfessionalAdapterPort;
import br.com.brunogodoif.contacthub.core.services.exceptions.InvalidParamUUIDTypeException;
import br.com.brunogodoif.contacthub.core.services.exceptions.ProfessionalNotFoundException;
import br.com.brunogodoif.contacthub.utils.validators.UuidValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public class ProfessionalFindService implements ProfessionalFindServicePort {
    private final ProfessionalAdapterPort professionalAdapter;

    public ProfessionalFindService(ProfessionalAdapterPort professionalAdapter) {
        this.professionalAdapter = professionalAdapter;
    }

    @Override
    public ListingProfessionalsResponse findAllWithPaginate(ProfessionalSearchListing professionalSearchListing, PaginationRequest paginationRequest) {
        Page<ProfessionalListingDomain> professionalPaginate = professionalAdapter.paginate(professionalSearchListing, paginationRequest);
        PaginationResponse pagination = PaginationResponse.getInstance(paginationRequest, Math.toIntExact(professionalPaginate.getTotalElements()));
        return new ListingProfessionalsResponse(professionalPaginate.getContent(), pagination);
    }

    @Override
    public ProfessionalDomain findById(String id) {
        if (StringUtils.isEmpty(id))
            throw new InvalidParamUUIDTypeException("Param [id] not found");

        UuidValidator.isValidUUID(id);

        Optional<ProfessionalDomain> professionalDomainOptional = professionalAdapter.getById(UUID.fromString(id));

        return professionalDomainOptional.orElseThrow(() -> new ProfessionalNotFoundException("Professional with id [" + id + "] not found"));

    }
}
