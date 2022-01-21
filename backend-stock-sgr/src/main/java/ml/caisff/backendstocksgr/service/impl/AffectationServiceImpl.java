package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Affectation;
import ml.caisff.backendstocksgr.repository.AffectationRepository;
import ml.caisff.backendstocksgr.service.AffectationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Affectation}.
 */
@Service
@Transactional
public class AffectationServiceImpl implements AffectationService {

    private final Logger log = LoggerFactory.getLogger(AffectationServiceImpl.class);

    private final AffectationRepository affectationRepository;

    public AffectationServiceImpl(AffectationRepository affectationRepository) {
        this.affectationRepository = affectationRepository;
    }

    @Override
    public Affectation save(Affectation affectation) {
        log.debug("Request to save Affectation : {}", affectation);
        return affectationRepository.save(affectation);
    }

    @Override
    public Optional<Affectation> partialUpdate(Affectation affectation) {
        log.debug("Request to partially update Affectation : {}", affectation);

        return affectationRepository
            .findById(affectation.getId())
            .map(existingAffectation -> {
                if (affectation.getDateAffectation() != null) {
                    existingAffectation.setDateAffectation(affectation.getDateAffectation());
                }
                if (affectation.getQuantite() != null) {
                    existingAffectation.setQuantite(affectation.getQuantite());
                }
                if (affectation.getCreatedAt() != null) {
                    existingAffectation.setCreatedAt(affectation.getCreatedAt());
                }
                if (affectation.getUpdateAt() != null) {
                    existingAffectation.setUpdateAt(affectation.getUpdateAt());
                }

                return existingAffectation;
            })
            .map(affectationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Affectation> findAll() {
        log.debug("Request to get all Affectations");
        return affectationRepository.findAllWithEagerRelationships();
    }

    public Page<Affectation> findAllWithEagerRelationships(Pageable pageable) {
        return affectationRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Affectation> findOne(Long id) {
        log.debug("Request to get Affectation : {}", id);
        return affectationRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Affectation : {}", id);
        affectationRepository.deleteById(id);
    }
}
