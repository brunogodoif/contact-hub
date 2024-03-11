package br.com.brunogodoif.contacthub.core.ports.inbound;

import br.com.brunogodoif.contacthub.core.domain.ProfessionalDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalCreateDomain;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalUpdateDomain;

public interface ProfessionalPersistenceServicePort {

    ProfessionalDomain create(ProfessionalCreateDomain professionalCreateDomain);
    ProfessionalDomain update(ProfessionalUpdateDomain professionalCreateDomain);

    boolean deleteById(String id);

}
