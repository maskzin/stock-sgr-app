package ml.caisff.backendstocksgr.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.AffectationArticle;
import ml.caisff.backendstocksgr.repository.AffectationArticleRepository;
import ml.caisff.backendstocksgr.service.AffectationArticleService;
import ml.caisff.backendstocksgr.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ml.caisff.backendstocksgr.domain.AffectationArticle}.
 */
@RestController
@RequestMapping("/api")
public class AffectationArticleResource {

    private final Logger log = LoggerFactory.getLogger(AffectationArticleResource.class);

    private static final String ENTITY_NAME = "affectationArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AffectationArticleService affectationArticleService;

    private final AffectationArticleRepository affectationArticleRepository;

    public AffectationArticleResource(
        AffectationArticleService affectationArticleService,
        AffectationArticleRepository affectationArticleRepository
    ) {
        this.affectationArticleService = affectationArticleService;
        this.affectationArticleRepository = affectationArticleRepository;
    }

    /**
     * {@code POST  /affectation-articles} : Create a new affectationArticle.
     *
     * @param affectationArticle the affectationArticle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new affectationArticle, or with status {@code 400 (Bad Request)} if the affectationArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/affectation-articles")
    public ResponseEntity<AffectationArticle> createAffectationArticle(@RequestBody AffectationArticle affectationArticle)
        throws URISyntaxException {
        log.debug("REST request to save AffectationArticle : {}", affectationArticle);
        if (affectationArticle.getId() != null) {
            throw new BadRequestAlertException("A new affectationArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AffectationArticle result = affectationArticleService.save(affectationArticle);
        return ResponseEntity
            .created(new URI("/api/affectation-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /affectation-articles/:id} : Updates an existing affectationArticle.
     *
     * @param id the id of the affectationArticle to save.
     * @param affectationArticle the affectationArticle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationArticle,
     * or with status {@code 400 (Bad Request)} if the affectationArticle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the affectationArticle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/affectation-articles/{id}")
    public ResponseEntity<AffectationArticle> updateAffectationArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AffectationArticle affectationArticle
    ) throws URISyntaxException {
        log.debug("REST request to update AffectationArticle : {}, {}", id, affectationArticle);
        if (affectationArticle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationArticle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AffectationArticle result = affectationArticleService.save(affectationArticle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationArticle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /affectation-articles/:id} : Partial updates given fields of an existing affectationArticle, field will ignore if it is null
     *
     * @param id the id of the affectationArticle to save.
     * @param affectationArticle the affectationArticle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated affectationArticle,
     * or with status {@code 400 (Bad Request)} if the affectationArticle is not valid,
     * or with status {@code 404 (Not Found)} if the affectationArticle is not found,
     * or with status {@code 500 (Internal Server Error)} if the affectationArticle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/affectation-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AffectationArticle> partialUpdateAffectationArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody AffectationArticle affectationArticle
    ) throws URISyntaxException {
        log.debug("REST request to partial update AffectationArticle partially : {}, {}", id, affectationArticle);
        if (affectationArticle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, affectationArticle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!affectationArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AffectationArticle> result = affectationArticleService.partialUpdate(affectationArticle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, affectationArticle.getId().toString())
        );
    }

    /**
     * {@code GET  /affectation-articles} : get all the affectationArticles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of affectationArticles in body.
     */
    @GetMapping("/affectation-articles/epave")
    public List<AffectationArticle> findByEpave(){
        return affectationArticleService.findByEpave();
    }

    @GetMapping("/affectation-articles")
    public List<AffectationArticle> getAllAffectationArticles() {
        log.debug("REST request to get all AffectationArticles");
        return affectationArticleService.findAll();
    }

    @GetMapping("/affectation-articles/validAt")
    public List<AffectationArticle> findAffectationArticleByValidity() {
        log.debug("REST request to get all AffectationArticles Validity");
        return affectationArticleService.findAffectationArticleByIsValidatidy();
    }

    @GetMapping("/affectation-articles/articleId/{id}")
    public List<AffectationArticle> findAffectationArticleById(@PathVariable Long id){
        log.debug("REST request to get  AffectationArticles by id",id);
        return affectationArticleService.findAffectationArticleById(id);
    }

    /**
     * {@code GET  /affectation-articles/:id} : get the "id" affectationArticle.
     *
     * @param id the id of the affectationArticle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the affectationArticle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/affectation-articles/{id}")
    public ResponseEntity<AffectationArticle> getAffectationArticle(@PathVariable Long id) {
        log.debug("REST request to get AffectationArticle : {}", id);
        Optional<AffectationArticle> affectationArticle = affectationArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(affectationArticle);
    }

    /**
     * {@code DELETE  /affectation-articles/:id} : delete the "id" affectationArticle.
     *
     * @param id the id of the affectationArticle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/affectation-articles/{id}")
    public ResponseEntity<Void> deleteAffectationArticle(@PathVariable Long id) {
        log.debug("REST request to delete AffectationArticle : {}", id);
        affectationArticleService.delete(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
