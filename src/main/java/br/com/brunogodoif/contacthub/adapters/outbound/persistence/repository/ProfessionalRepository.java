package br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto.ProfessionalEntityListingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessionalRepository extends JpaRepository<ProfessionalEntity, UUID> {
    @Query("SELECT new br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository.dto." +
            "ProfessionalEntityListingRecord(p.id AS id, p.name, p.role, p.createdAt, COUNT(c))" +
            " FROM ProfessionalEntity p LEFT JOIN p.contacts c GROUP BY p")
    Page<ProfessionalEntityListingRecord> findAllWithContactCount(Pageable pageable);
}
