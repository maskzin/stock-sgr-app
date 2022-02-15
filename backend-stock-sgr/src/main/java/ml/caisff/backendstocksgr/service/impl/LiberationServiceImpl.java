package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ml.caisff.backendstocksgr.domain.Liberation;
import ml.caisff.backendstocksgr.repository.LiberationRepository;
import ml.caisff.backendstocksgr.service.LiberationService;

/**
 * Service Implementation for managing {@link Liberation}.
 */
@Service
@Transactional
public class LiberationServiceImpl implements LiberationService {

    private final Logger log = LoggerFactory.getLogger(LiberationServiceImpl.class);

    private final LiberationRepository liberationRepository;

    public LiberationServiceImpl(LiberationRepository liberationRepository) {
        this.liberationRepository = liberationRepository;
    }

    @Override
    public Liberation save(Liberation liberation) {
        log.debug("Request to save Liberation : {}", liberation);
        return liberationRepository.save(liberation);
    }

    @Override
    public Optional<Liberation> partialUpdate(Liberation liberation) {
        log.debug("Request to partially update Liberation : {}", liberation);

        return liberationRepository
            .findById(liberation.getId())
            .map(existingLiberation -> {
                if (liberation.getDate() != null) {
                    existingLiberation.setDate(liberation.getDate());
                }
                if (liberation.getEtat() != null) {
                    existingLiberation.setEtat(liberation.getEtat());
                }

                return existingLiberation;
            })
            .map(liberationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Liberation> findAll() {
        log.debug("Request to get all Liberations");
        return liberationRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Liberation> findOne(Long id) {
        log.debug("Request to get Liberation : {}", id);
        return liberationRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Liberation : {}", id);
        liberationRepository.deleteById(id);
    }
}