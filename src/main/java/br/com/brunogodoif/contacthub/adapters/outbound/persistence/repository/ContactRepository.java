package br.com.brunogodoif.contacthub.adapters.outbound.persistence.repository;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ContactEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ContactRepository extends JpaRepository<ContactEntity, UUID> {

    @Query("SELECT c from ContactEntity c WHERE c.professional.id = :professionalId")
    List<ContactEntity> findAllByProfessionalId(@Param("professionalId") UUID professionalId, Sort sort);

    @Query("SELECT COUNT(c) FROM ContactEntity c WHERE c.professional.id = :professionalId")
    Long countByProfessionalId(@Param("professionalId") UUID professionalId);
}
