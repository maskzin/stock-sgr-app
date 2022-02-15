package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Fournisseur;
import ml.caisff.backendstocksgr.repository.FournisseurRepository;
import ml.caisff.backendstocksgr.service.FournisseurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fournisseur}.
 */
@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    private final Logger log = LoggerFactory.getLogger(FournisseurServiceImpl.class);

    private final FournisseurRepository fournisseurRepository;

    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository) {
        this.fournisseurRepository = fournisseurRepository;
    }

    @Override
    public Fournisseur save(Fournisseur fournisseur) {
        log.debug("Request to save Fournisseur : {}", fournisseur);
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public Optional<Fournisseur> partialUpdate(Fournisseur fournisseur) {
        log.debug("Request to partially update Fournisseur : {}", fournisseur);

        return fournisseurRepository
            .findById(fournisseur.getId())
            .map(existingFournisseur -> {
                if (fournisseur.getNom() != null) {
                    existingFournisseur.setNom(fournisseur.getNom());
                }
                if (fournisseur.getNif() != null) {
                    existingFournisseur.setNif(fournisseur.getNif());
                }
                if (fournisseur.getTelehone() != null) {
                    existingFournisseur.setTelehone(fournisseur.getTelehone());
                }
                if (fournisseur.getAdresse() != null) {
                    existingFournisseur.setAdresse(fournisseur.getAdresse());
                }
                if (fournisseur.getCreatedAt() != null) {
                    existingFournisseur.setCreatedAt(fournisseur.getCreatedAt());
                }
                if (fournisseur.getUpdateAt() != null) {
                    existingFournisseur.setUpdateAt(fournisseur.getUpdateAt());
                }

                return existingFournisseur;
            })
            .map(fournisseurRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fournisseur> findAll() {
        log.debug("Request to get all Fournisseurs");
        return fournisseurRepository.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fournisseur> findOne(Long id) {
        log.debug("Request to get Fournisseur : {}", id);
        return fournisseurRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fournisseur : {}", id);
        fournisseurRepository.deleteById(id);
    }
}
