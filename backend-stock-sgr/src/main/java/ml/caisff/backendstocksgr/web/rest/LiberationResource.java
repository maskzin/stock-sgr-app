package ml.caisff.backendstocksgr.web.rest;

import ml.caisff.backendstocksgr.domain.Liberation;
import ml.caisff.backendstocksgr.repository.LiberationRepository;
import ml.caisff.backendstocksgr.service.LiberationService;
import ml.caisff.backendstocksgr.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Liberation}.
 */
@RestController
@RequestMapping("/api")
public class LiberationResource {

    private final Logger log = LoggerFactory.getLogger(LiberationResource.class);

    private static final String ENTITY_NAME = "liberation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LiberationService liberationService;

    private final LiberationRepository liberationRepository;

    public LiberationResource(LiberationService liberationService, LiberationRepository liberationRepository) {
        this.liberationService = liberationService;
        this.liberationRepository = liberationRepository;
    }

    /**
     * {@code POST  /liberations} : Create a new liberation.
     *
     * @param liberation the liberation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new liberation, or with status {@code 400 (Bad Request)} if the liberation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/liberations")
    public ResponseEntity<Liberation> createLiberation(@RequestBody Liberation liberation) throws URISyntaxException {
        log.debug("REST request to save Liberation : {}", liberation);
        if (liberation.getId() != null) {
            throw new BadRequestAlertException("A new liberation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Liberation result = liberationService.save(liberation);
        return ResponseEntity
            .created(new URI("/api/liberations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /liberations/:id} : Updates an existing liberation.
     *
     * @param id the id of the liberation to save.
     * @param liberation the liberation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liberation,
     * or with status {@code 400 (Bad Request)} if the liberation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the liberation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/liberations/{id}")
    public ResponseEntity<Liberation> updateLiberation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Liberation liberation
    ) throws URISyntaxException {
        log.debug("REST request to update Liberation : {}, {}", id, liberation);
        if (liberation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liberation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!liberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Liberation result = liberationService.save(liberation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liberation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /liberations/:id} : Partial updates given fields of an existing liberation, field will ignore if it is null
     *
     * @param id the id of the liberation to save.
     * @param liberation the liberation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated liberation,
     * or with status {@code 400 (Bad Request)} if the liberation is not valid,
     * or with status {@code 404 (Not Found)} if the liberation is not found,
     * or with status {@code 500 (Internal Server Error)} if the liberation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/liberations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Liberation> partialUpdateLiberation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Liberation liberation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Liberation partially : {}, {}", id, liberation);
        if (liberation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, liberation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!liberationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Liberation> result = liberationService.partialUpdate(liberation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, liberation.getId().toString())
        );
    }

    /**
     * {@code GET  /liberations} : get all the liberations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of liberations in body.
     */
    @GetMapping("/liberations")
    public List<Liberation> getAllLiberations() {
        log.debug("REST request to get all Liberations");
        return liberationService.findAll();
    }

    /**
     * {@code GET  /liberations/:id} : get the "id" liberation.
     *
     * @param id the id of the liberation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the liberation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/liberations/{id}")
    public ResponseEntity<Liberation> getLiberation(@PathVariable Long id) {
        log.debug("REST request to get Liberation : {}", id);
        Optional<Liberation> liberation = liberationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(liberation);
    }

    /**
     * {@code DELETE  /liberations/:id} : delete the "id" liberation.
     *
     * @param id the id of the liberation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/liberations/{id}")
    public ResponseEntity<Void> deleteLiberation(@PathVariable Long id) {
        log.debug("REST request to delete Liberation : {}", id);
        liberationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
