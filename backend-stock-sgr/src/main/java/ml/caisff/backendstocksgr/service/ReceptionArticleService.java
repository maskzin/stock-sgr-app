package ml.caisff.backendstocksgr.service;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.ReceptionArticle;

/**
 * Service Interface for managing {@link ReceptionArticle}.
 */
public interface ReceptionArticleService {
    /**
     * Save a receptionArticle.
     *
     * @param receptionArticle the entity to save.
     * @return the persisted entity.
     */
    ReceptionArticle save(ReceptionArticle receptionArticle);

    /**
     * Partially updates a receptionArticle.
     *
     * @param receptionArticle the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReceptionArticle> partialUpdate(ReceptionArticle receptionArticle);

    /**
     * Get all the receptionArticles.
     *
     * @return the list of entities.
     */
    List<ReceptionArticle> findAll();

    List<ReceptionArticle> findReceptionArticleById(Long id);

    /**
     * Get the "id" receptionArticle.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReceptionArticle> findOne(Long id);

    /**
     * Delete the "id" receptionArticle.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
