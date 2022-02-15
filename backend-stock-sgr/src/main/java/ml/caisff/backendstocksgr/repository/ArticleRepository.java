package ml.caisff.backendstocksgr.repository;

import ml.caisff.backendstocksgr.domain.Article;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value="select (coalesce((select sum(quantite) from reception_article where article_id=:id),0)-coalesce((select sum(quantite) from affectation_article where is_validity=false AND libere=false AND article_id=:id),0)) as quantite_restant from article where id=:id", nativeQuery = true)
    public Long findQuantiteRestant(@Param("id") Long id);
    public List<Article> findByMarqueId(Long id);
    public List<Article> findByCategorieId(Long id);

}
