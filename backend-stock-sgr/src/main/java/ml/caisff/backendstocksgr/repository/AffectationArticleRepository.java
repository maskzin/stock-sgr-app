package ml.caisff.backendstocksgr.repository;

import ml.caisff.backendstocksgr.domain.AffectationArticle;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the AffectationArticle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AffectationArticleRepository extends JpaRepository<AffectationArticle, Long> {

    List<AffectationArticle> findAffectationArticleByArticleId(Long id);
    List<AffectationArticle> findByIsValidity(boolean validity);
    List<AffectationArticle> findByEpave(Sort sort,boolean epave);
    List<AffectationArticle> findByValidAtNotNull();
}
