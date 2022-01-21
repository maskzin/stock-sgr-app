package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.ReceptionArticle;
import ml.caisff.backendstocksgr.repository.ReceptionArticleRepository;
import ml.caisff.backendstocksgr.service.ReceptionArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ReceptionArticle}.
 */
@Service
@Transactional
public class ReceptionArticleServiceImpl implements ReceptionArticleService {

    private final Logger log = LoggerFactory.getLogger(ReceptionArticleServiceImpl.class);

    private final ReceptionArticleRepository receptionArticleRepository;

    public ReceptionArticleServiceImpl(ReceptionArticleRepository receptionArticleRepository) {
        this.receptionArticleRepository = receptionArticleRepository;
    }

    @Override
    public ReceptionArticle save(ReceptionArticle receptionArticle) {
        log.debug("Request to save ReceptionArticle : {}", receptionArticle);
        return receptionArticleRepository.save(receptionArticle);
    }

    @Override
    public Optional<ReceptionArticle> partialUpdate(ReceptionArticle receptionArticle) {
        log.debug("Request to partially update ReceptionArticle : {}", receptionArticle);

        return receptionArticleRepository
            .findById(receptionArticle.getId())
            .map(existingReceptionArticle -> {
                if (receptionArticle.getIsReception() != null) {
                    existingReceptionArticle.setIsReception(receptionArticle.getIsReception());
                }
                if (receptionArticle.getQuantite() != null) {
                    existingReceptionArticle.setQuantite(receptionArticle.getQuantite());
                }
                if (receptionArticle.getCreatedAt() != null) {
                    existingReceptionArticle.setCreatedAt(receptionArticle.getCreatedAt());
                }
                if (receptionArticle.getUpdateAt() != null) {
                    existingReceptionArticle.setUpdateAt(receptionArticle.getUpdateAt());
                }

                return existingReceptionArticle;
            })
            .map(receptionArticleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReceptionArticle> findAll() {
        log.debug("Request to get all ReceptionArticles");
        return receptionArticleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReceptionArticle> findOne(Long id) {
        log.debug("Request to get ReceptionArticle : {}", id);
        return receptionArticleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReceptionArticle : {}", id);
        receptionArticleRepository.deleteById(id);
    }
}
