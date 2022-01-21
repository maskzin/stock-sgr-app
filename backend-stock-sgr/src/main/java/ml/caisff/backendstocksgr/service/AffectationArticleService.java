package ml.caisff.backendstocksgr.service;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.AffectationArticle;

/**
 * Service Interface for managing {@link AffectationArticle}.
 */
public interface AffectationArticleService {
    /**
     * Save a affectationArticle.
     *
     * @param affectationArticle the entity to save.
     * @return the persisted entity.
     */
    AffectationArticle save(AffectationArticle affectationArticle);

    /**
     * Partially updates a affectationArticle.
     *
     * @param affectationArticle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AffectationArticle> partialUpdate(AffectationArticle affectationArticle);

    /**
     * Get all the affectationArticles.
     *
     * @return the list of entities.
     */
    List<AffectationArticle> findAll();

    /**
     * Get the "id" affectationArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AffectationArticle> findOne(Long id);

    /**
     * Delete the "id" affectationArticle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
