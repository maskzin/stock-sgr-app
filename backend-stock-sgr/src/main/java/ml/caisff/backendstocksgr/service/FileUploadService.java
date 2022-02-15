package ml.caisff.backendstocksgr.service;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.FileUpload;

/**
 * Service Interface for managing {@link FileUpload}.
 */
public interface FileUploadService {
    /**
     * Save a fileUpload.
     *
     * @param fileUpload the entity to save.
     * @return the persisted entity.
     */
    FileUpload save(FileUpload fileUpload);

    /**
     * Partially updates a fileUpload.
     *
     * @param fileUpload the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FileUpload> partialUpdate(FileUpload fileUpload);

    /**
     * Get all the fileUploads.
     *
     * @return the list of entities.
     */
    List<FileUpload> findAll();

    /**
     * Get the "id" fileUpload.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FileUpload> findOne(Long id);

    /**
     * Delete the "id" fileUpload.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
