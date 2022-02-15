package ml.caisff.backendstocksgr.repository;

import ml.caisff.backendstocksgr.domain.ReceptionArticle;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ReceptionArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReceptionArticleRepository extends JpaRepository<ReceptionArticle, Long> {

    List<ReceptionArticle> findReceptionArticleByArticleId(Long id);
}
