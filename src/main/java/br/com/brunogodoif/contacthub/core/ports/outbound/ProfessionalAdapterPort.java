package br.com.brunogodoif.contacthub.core.ports.outbound;


import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.ProfessionalListingDomain;
import br.com.brunogodoif.contacthub.core.domain.pagination.PaginationRequest;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalUpdateDomain;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ProfessionalAdapterPort {
    ProfessionalDomain create(ProfessionalCreateDomain professionalToCreate);

    boolean update(ProfessionalUpdateDomain professionalToUpdate);

    Page<ProfessionalListingDomain> paginate(PaginationRequest paginationRequest);

    Optional<ProfessionalDomain> getById(UUID id);

    boolean deleteById(UUID id);

}
