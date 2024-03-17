package br.com.brunogodoif.contacthub.adapters.outbound.persistence.specifications;

import br.com.brunogodoif.contacthub.adapters.outbound.persistence.entities.ProfessionalEntity;
import br.com.brunogodoif.contacthub.core.domain.request.ProfessionalSearchListing;
import jakarta.persistence.criteria.Predicate;
import net.kaczmarzyk.spring.data.jpa.domain.Between;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    @And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "role", spec = LikeIgnoreCase.class),
            @Spec(path = "createdAt", spec = Between.class)
    })
    public interface ProfessionalsSpec extends Specification<ProfessionalEntity> {
    }

    public static ProfessionalsSpec convertToProfessionalsSpec(ProfessionalSearchListing professionalSearchListing) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (professionalSearchListing.getName() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + professionalSearchListing.getName().toLowerCase() + "%"
                        )
                );
            }

            if (professionalSearchListing.getRole() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("role")),
                                "%" + professionalSearchListing.getRole().toLowerCase() + "%"
                        )
                );
            }

            if (professionalSearchListing.getStartCreatedAt() != null && professionalSearchListing.getEndCreatedAt() != null) {
                predicate = criteriaBuilder.and(
                        predicate,
                        criteriaBuilder.between(root.get("createdAt"), professionalSearchListing.getStartCreatedAt(), professionalSearchListing.getEndCreatedAt())
                );
            }

            return predicate;
        };
    }


}
