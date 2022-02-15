package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.FileUpload;
import ml.caisff.backendstocksgr.repository.FileUploadRepository;
import ml.caisff.backendstocksgr.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FileUpload}.
 */
@Service
@Transactional
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger log = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final FileUploadRepository fileUploadRepository;

    public FileUploadServiceImpl(FileUploadRepository fileUploadRepository) {
        this.fileUploadRepository = fileUploadRepository;
    }

    @Override
    public FileUpload save(FileUpload fileUpload) {
        log.debug("Request to save FileUpload : {}", fileUpload);
        return fileUploadRepository.save(fileUpload);
    }

    @Override
    public Optional<FileUpload> partialUpdate(FileUpload fileUpload) {
        log.debug("Request to partially update FileUpload : {}", fileUpload);

        return fileUploadRepository
            .findById(fileUpload.getId())
            .map(existingFileUpload -> {
                if (fileUpload.getName() != null) {
                    existingFileUpload.setName(fileUpload.getName());
                }
                if (fileUpload.getPath() != null) {
                    existingFileUpload.setPath(fileUpload.getPath());
                }
                if (fileUpload.getTaille() != null) {
                    existingFileUpload.setTaille(fileUpload.getTaille());
                }
                if (fileUpload.getType() != null) {
                    existingFileUpload.setType(fileUpload.getType());
                }
                if (fileUpload.getData() != null) {
                    existingFileUpload.setData(fileUpload.getData());
                }
                if (fileUpload.getCreatedAt() != null) {
                    existingFileUpload.setCreatedAt(fileUpload.getCreatedAt());
                }
                if (fileUpload.getUpdateAt() != null) {
                    existingFileUpload.setUpdateAt(fileUpload.getUpdateAt());
                }

                return existingFileUpload;
            })
            .map(fileUploadRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileUpload> findAll() {
        log.debug("Request to get all FileUploads");
        return fileUploadRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileUpload> findOne(Long id) {
        log.debug("Request to get FileUpload : {}", id);
        return fileUploadRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FileUpload : {}", id);
        fileUploadRepository.deleteById(id);
    }
}
