package ml.caisff.backendstocksgr.repository;

import ml.caisff.backendstocksgr.domain.AffectationArticle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AffectationArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationArticleRepository extends JpaRepository<AffectationArticle, Long> {}
