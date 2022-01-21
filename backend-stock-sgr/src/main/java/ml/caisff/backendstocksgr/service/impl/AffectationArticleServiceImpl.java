package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.AffectationArticle;
import ml.caisff.backendstocksgr.repository.AffectationArticleRepository;
import ml.caisff.backendstocksgr.service.AffectationArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AffectationArticle}.
 */
@Service
@Transactional
public class AffectationArticleServiceImpl implements AffectationArticleService {

    private final Logger log = LoggerFactory.getLogger(AffectationArticleServiceImpl.class);

    private final AffectationArticleRepository affectationArticleRepository;

    public AffectationArticleServiceImpl(AffectationArticleRepository affectationArticleRepository) {
        this.affectationArticleRepository = affectationArticleRepository;
    }

    @Override
    public AffectationArticle save(AffectationArticle affectationArticle) {
        log.debug("Request to save AffectationArticle : {}", affectationArticle);
        return affectationArticleRepository.save(affectationArticle);
    }

    @Override
    public Optional<AffectationArticle> partialUpdate(AffectationArticle affectationArticle) {
        log.debug("Request to partially update AffectationArticle : {}", affectationArticle);

        return affectationArticleRepository
            .findById(affectationArticle.getId())
            .map(existingAffectationArticle -> {
                if (affectationArticle.getIsAffectation() != null) {
                    existingAffectationArticle.setIsAffectation(affectationArticle.getIsAffectation());
                }
                if (affectationArticle.getQuantite() != null) {
                    existingAffectationArticle.setQuantite(affectationArticle.getQuantite());
                }
                if (affectationArticle.getCreatedAt() != null) {
                    existingAffectationArticle.setCreatedAt(affectationArticle.getCreatedAt());
                }
                if (affectationArticle.getUpdateAt() != null) {
                    existingAffectationArticle.setUpdateAt(affectationArticle.getUpdateAt());
                }

                return existingAffectationArticle;
            })
            .map(affectationArticleRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AffectationArticle> findAll() {
        log.debug("Request to get all AffectationArticles");
        return affectationArticleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AffectationArticle> findOne(Long id) {
        log.debug("Request to get AffectationArticle : {}", id);
        return affectationArticleRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AffectationArticle : {}", id);
        affectationArticleRepository.deleteById(id);
    }
}
