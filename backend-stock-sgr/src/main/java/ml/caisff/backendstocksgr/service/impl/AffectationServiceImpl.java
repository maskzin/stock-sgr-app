package ml.caisff.backendstocksgr.service.impl;

import java.time.LocalDate;
import java.util.*;

import ml.caisff.backendstocksgr.domain.Affectation;
import ml.caisff.backendstocksgr.domain.AffectationArticle;
import ml.caisff.backendstocksgr.domain.FileUpload;
import ml.caisff.backendstocksgr.repository.AffectationArticleRepository;
import ml.caisff.backendstocksgr.repository.AffectationRepository;
import ml.caisff.backendstocksgr.service.AffectationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final AffectationArticleRepository affectationArticleRepository;

    public AffectationServiceImpl(AffectationRepository affectationRepository,AffectationArticleRepository affectationArticleRepository) {
        this.affectationRepository = affectationRepository;
        this.affectationArticleRepository = affectationArticleRepository;
    }

    public Affectation save(Affectation affectation) {
        log.debug("Request to save Affectation : {}", affectation);
        Set<AffectationArticle> affectationArticles = affectation.getAffectationArticles();
        Set<AffectationArticle> affectationArticleSet = new HashSet<>();
        for (AffectationArticle affectationArticle:affectationArticles) {
               if (affectationArticle.getId()!=null){
                   Optional<AffectationArticle> affectationArticle1 = affectationArticleRepository.findById(affectationArticle.getId());
                   if (affectationArticle1.isPresent()){
                       affectationArticle.setValidAt(LocalDate.now());
                       affectationArticle.setIsValidity(true);
                       affectationArticleRepository.save(affectationArticle);
                   }
               }
                AffectationArticle affectationArticle2 = new AffectationArticle();
                affectationArticle2.setIsValidity(false);
                affectationArticle2.setIsAffectation(affectationArticle.getIsAffectation());
                affectationArticle2.setValidAt(null);
                affectationArticle2.setLibere(false);
                affectationArticle2.setEpave(false);
                affectationArticle2.setReference(affectationArticle.getReference());
                affectationArticle2.setIsAffectation(affectationArticle.getIsAffectation());
                affectationArticle2.setQuantite(affectationArticle.getQuantite());
                affectationArticle2.setArticle(affectationArticle.getArticle());
                affectationArticle2.setAffectations(affectationArticle.getAffectations());
                affectationArticleSet.add(affectationArticle2);


        }
        if (affectation.getId()!=null){
            Affectation affectation1 =affectationRepository.getById(affectation.getId());
            affectation1.setStatus("reaffecte");
            affectationRepository.save(affectation1);
        }
        affectation.setId(null);
        affectation.setStatus("affecte");
        Set <FileUpload> files = affectation.getFileUploads();
        Set<FileUpload> fileUploads = new HashSet<>();
        for (FileUpload file:files) {
            file.setId(null);
            fileUploads.add(file);
        }
        affectation.setFileUploads(fileUploads);
        affectation.setAffectationArticles(affectationArticleSet);
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
                if (affectation.getExterne()) {
                    existingAffectation.setExterne(affectation.getExterne());
                }
                if (affectation.getStructure() != null) {
                    existingAffectation.setStructure(affectation.getStructure());
                }
                if (affectation.getAffectant() != null) {
                    existingAffectation.setAffectant(affectation.getAffectant());
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
        List<Affectation> affectations = affectationRepository.findAllWithEagerRelationships(Sort.by(Sort.Direction.DESC,"id"));
        List<Affectation> affectations1 = new ArrayList<>();
        for (Affectation affectation: affectations) {
            Set<AffectationArticle> affectationArticles = affectation.getAffectationArticles();
            Set<AffectationArticle> affectationArticles1 = new HashSet<>();
            for (AffectationArticle affectationArticle:affectationArticles) {
                   if(affectationArticle.getEpave()==false){
                    if (affectationArticle.getLibere()==false){
                        if (affectationArticle.getValidAt()==null){
                            affectationArticles1.add(affectationArticle);
                        }
                    }
                   }
               /* if (affectationArticle.getValidAt()==null){
                    affectationArticles1.add(affectationArticle);
                }*/

            }
            if (affectationArticles1.size()!=0){
                affectation.setAffectationArticles(affectationArticles1);
                affectations1.add(affectation);
            }
        }
        return affectations1;
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

    @Override
    public List<Affectation> findAllAffectation() {
        return affectationRepository.findAllWithEagerRelationships(Sort.by(Sort.Direction.DESC,"id"));
    }
}
