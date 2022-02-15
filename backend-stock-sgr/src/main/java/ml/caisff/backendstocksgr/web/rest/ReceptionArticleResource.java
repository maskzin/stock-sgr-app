package ml.caisff.backendstocksgr.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import ml.caisff.backendstocksgr.domain.ReceptionArticle;
import ml.caisff.backendstocksgr.repository.ReceptionArticleRepository;
import ml.caisff.backendstocksgr.service.ReceptionArticleService;
import ml.caisff.backendstocksgr.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ml.caisff.backendstocksgr.domain.ReceptionArticle}.
 */
@RestController
@RequestMapping("/api")
public class ReceptionArticleResource {

    private final Logger log = LoggerFactory.getLogger(ReceptionArticleResource.class);

    private static final String ENTITY_NAME = "receptionArticle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReceptionArticleService receptionArticleService;

    private final ReceptionArticleRepository receptionArticleRepository;

    public ReceptionArticleResource(
        ReceptionArticleService receptionArticleService,
        ReceptionArticleRepository receptionArticleRepository
    ) {
        this.receptionArticleService = receptionArticleService;
        this.receptionArticleRepository = receptionArticleRepository;
    }

    /**
     * {@code POST  /reception-articles} : Create a new receptionArticle.
     *
     * @param receptionArticle the receptionArticle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new receptionArticle, or with status {@code 400 (Bad Request)} if the receptionArticle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reception-articles")
    public ResponseEntity<ReceptionArticle> createReceptionArticle(@RequestBody ReceptionArticle receptionArticle)
        throws URISyntaxException {
        log.debug("REST request to save ReceptionArticle : {}", receptionArticle);
        if (receptionArticle.getId() != null) {
            throw new BadRequestAlertException("A new receptionArticle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReceptionArticle result = receptionArticleService.save(receptionArticle);
        return ResponseEntity
            .created(new URI("/api/reception-articles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reception-articles/:id} : Updates an existing receptionArticle.
     *
     * @param id the id of the receptionArticle to save.
     * @param receptionArticle the receptionArticle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionArticle,
     * or with status {@code 400 (Bad Request)} if the receptionArticle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the receptionArticle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reception-articles/{id}")
    public ResponseEntity<ReceptionArticle> updateReceptionArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceptionArticle receptionArticle
    ) throws URISyntaxException {
        log.debug("REST request to update ReceptionArticle : {}, {}", id, receptionArticle);
        if (receptionArticle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionArticle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReceptionArticle result = receptionArticleService.save(receptionArticle);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionArticle.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reception-articles/:id} : Partial updates given fields of an existing receptionArticle, field will ignore if it is null
     *
     * @param id the id of the receptionArticle to save.
     * @param receptionArticle the receptionArticle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated receptionArticle,
     * or with status {@code 400 (Bad Request)} if the receptionArticle is not valid,
     * or with status {@code 404 (Not Found)} if the receptionArticle is not found,
     * or with status {@code 500 (Internal Server Error)} if the receptionArticle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reception-articles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReceptionArticle> partialUpdateReceptionArticle(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReceptionArticle receptionArticle
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReceptionArticle partially : {}, {}", id, receptionArticle);
        if (receptionArticle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, receptionArticle.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!receptionArticleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReceptionArticle> result = receptionArticleService.partialUpdate(receptionArticle);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, receptionArticle.getId().toString())
        );
    }

    /**
     * {@code GET  /reception-articles} : get all the receptionArticles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of receptionArticles in body.
     */
    @GetMapping("/reception-articles")
    public List<ReceptionArticle> getAllReceptionArticles() {
        log.debug("REST request to get all ReceptionArticles");
        return receptionArticleService.findAll();
    }


    @GetMapping("/reception-articles/articleId/{id}")
    public List<ReceptionArticle> getReceptionByArticleId(@PathVariable Long id){
        return receptionArticleService.findReceptionArticleById(id);
    }

    /**
     * {@code GET  /reception-articles/:id} : get the "id" receptionArticle.
     *
     * @param id the id of the receptionArticle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the receptionArticle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reception-articles/{id}")
    public ResponseEntity<ReceptionArticle> getReceptionArticle(@PathVariable Long id) {
        log.debug("REST request to get ReceptionArticle : {}", id);
        Optional<ReceptionArticle> receptionArticle = receptionArticleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(receptionArticle);
    }



    /**
     * {@code DELETE  /reception-articles/:id} : delete the "id" receptionArticle.
     *
     * @param id the id of the receptionArticle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reception-articles/{id}")
    public ResponseEntity<Void> deleteReceptionArticle(@PathVariable Long id) {
        log.debug("REST request to delete ReceptionArticle : {}", id);
        receptionArticleService.delete(id);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
