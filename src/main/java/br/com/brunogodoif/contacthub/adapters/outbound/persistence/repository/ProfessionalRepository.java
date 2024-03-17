package br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProfessionalRepository extends JpaRepository<ProfessionalEntity, UUID>, JpaSpecificationExecutor<ProfessionalEntity> {
}
