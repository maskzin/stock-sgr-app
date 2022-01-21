package ml.caisff.backendstocksgr.service;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Article;

/**
 * Service Interface for managing {@link Article}.
 */
public interface ArticleService {
    /**
     * Save a article.
     *
     * @param article the entity to save.
     * @return the persisted entity.
     */
    Article save(Article article);

    /**
     * Partially updates a article.
     *
     * @param article the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Article> partialUpdate(Article article);

    /**
     * Get all the articles.
     *
     * @return the list of entities.
     */
    List<Article> findAll();

    /**
     * Get the "id" article.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Article> findOne(Long id);

    /**
     * Delete the "id" article.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
