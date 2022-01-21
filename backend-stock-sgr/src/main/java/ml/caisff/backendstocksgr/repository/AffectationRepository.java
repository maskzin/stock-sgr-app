package ml.caisff.backendstocksgr.repository;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Affectation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Affectation entity.
 */
@Repository
public interface AffectationRepository extends JpaRepository<Affectation, Long> {
    @Query(
        value = "select distinct affectation from Affectation affectation left join fetch affectation.affectationArticles",
        countQuery = "select count(distinct affectation) from Affectation affectation"
    )
    Page<Affectation> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct affectation from Affectation affectation left join fetch affectation.affectationArticles")
    List<Affectation> findAllWithEagerRelationships();

    @Query("select affectation from Affectation affectation left join fetch affectation.affectationArticles where affectation.id =:id")
    Optional<Affectation> findOneWithEagerRelationships(@Param("id") Long id);
}
