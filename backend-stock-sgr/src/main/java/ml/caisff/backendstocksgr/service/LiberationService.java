package ml.caisff.backendstocksgr.service;

import java.util.List;
import java.util.Optional;

import ml.caisff.backendstocksgr.domain.Liberation;

public interface LiberationService {
    /**
     * Save a liberation.
     *
     * @param liberation the entity to save.
     * @return the persisted entity.
     */
    Liberation save(Liberation liberation);

    /**
     * Partially updates a liberation.
     *
     * @param liberation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Liberation> partialUpdate(Liberation liberation);

    /**
     * Get all the liberations.
     *
     * @return the list of entities.
     */
    List<Liberation> findAll();

    /**
     * Get the "id" liberation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Liberation> findOne(Long id);

    /**
     * Delete the "id" liberation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

