package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Marque;
import ml.caisff.backendstocksgr.repository.MarqueRepository;
import ml.caisff.backendstocksgr.service.MarqueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Marque}.
 */
@Service
@Transactional
public class MarqueServiceImpl implements MarqueService {

    private final Logger log = LoggerFactory.getLogger(MarqueServiceImpl.class);

    private final MarqueRepository marqueRepository;

    public MarqueServiceImpl(MarqueRepository marqueRepository) {
        this.marqueRepository = marqueRepository;
    }

    @Override
    public Marque save(Marque marque) {
        log.debug("Request to save Marque : {}", marque);
        return marqueRepository.save(marque);
    }

    @Override
    public Optional<Marque> partialUpdate(Marque marque) {
        log.debug("Request to partially update Marque : {}", marque);

        return marqueRepository
            .findById(marque.getId())
            .map(existingMarque -> {
                if (marque.getLibelle() != null) {
                    existingMarque.setLibelle(marque.getLibelle());
                }

                return existingMarque;
            })
            .map(marqueRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Marque> findAll() {
        log.debug("Request to get all Marques");
        return marqueRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Marque> findOne(Long id) {
        log.debug("Request to get Marque : {}", id);
        return marqueRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Marque : {}", id);
        marqueRepository.deleteById(id);
    }
}
