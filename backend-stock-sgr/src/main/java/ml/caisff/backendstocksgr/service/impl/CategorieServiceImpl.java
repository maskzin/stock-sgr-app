package ml.caisff.backendstocksgr.service.impl;

import java.util.List;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.Categorie;
import ml.caisff.backendstocksgr.repository.CategorieRepository;
import ml.caisff.backendstocksgr.service.CategorieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Categorie}.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    private final CategorieRepository categorieRepository;

    public CategorieServiceImpl(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @Override
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        return categorieRepository.save(categorie);
    }

    @Override
    public Optional<Categorie> partialUpdate(Categorie categorie) {
        log.debug("Request to partially update Categorie : {}", categorie);

        return categorieRepository
            .findById(categorie.getId())
            .map(existingCategorie -> {
                if (categorie.getNom() != null) {
                    existingCategorie.setNom(categorie.getNom());
                }
                if (categorie.getCreatedAt() != null) {
                    existingCategorie.setCreatedAt(categorie.getCreatedAt());
                }
                if (categorie.getUpdateAt() != null) {
                    existingCategorie.setUpdateAt(categorie.getUpdateAt());
                }

                return existingCategorie;
            })
            .map(categorieRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categorie> findAll() {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
    }
}
