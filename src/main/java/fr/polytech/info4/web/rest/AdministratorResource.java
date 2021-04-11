package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.AdministratorRepository;
import fr.polytech.info4.service.AdministratorService;
import fr.polytech.info4.service.dto.AdministratorDTO;
import fr.polytech.info4.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.polytech.info4.domain.Administrator}.
 */
@RestController
@RequestMapping("/api")
public class AdministratorResource {

    private final Logger log = LoggerFactory.getLogger(AdministratorResource.class);

    private static final String ENTITY_NAME = "administrator";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministratorService administratorService;

    private final AdministratorRepository administratorRepository;

    public AdministratorResource(AdministratorService administratorService, AdministratorRepository administratorRepository) {
        this.administratorService = administratorService;
        this.administratorRepository = administratorRepository;
    }

    /**
     * {@code POST  /administrators} : Create a new administrator.
     *
     * @param administratorDTO the administratorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administratorDTO, or with status {@code 400 (Bad Request)} if the administrator has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/administrators")
    public ResponseEntity<AdministratorDTO> createAdministrator(@Valid @RequestBody AdministratorDTO administratorDTO)
        throws URISyntaxException {
        log.debug("REST request to save Administrator : {}", administratorDTO);
        if (administratorDTO.getId() != null) {
            throw new BadRequestAlertException("A new administrator cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdministratorDTO result = administratorService.save(administratorDTO);
        return ResponseEntity
            .created(new URI("/api/administrators/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /administrators/:id} : Updates an existing administrator.
     *
     * @param id the id of the administratorDTO to save.
     * @param administratorDTO the administratorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administratorDTO,
     * or with status {@code 400 (Bad Request)} if the administratorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administratorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/administrators/{id}")
    public ResponseEntity<AdministratorDTO> updateAdministrator(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdministratorDTO administratorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Administrator : {}, {}", id, administratorDTO);
        if (administratorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administratorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdministratorDTO result = administratorService.save(administratorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administratorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /administrators/:id} : Partial updates given fields of an existing administrator, field will ignore if it is null
     *
     * @param id the id of the administratorDTO to save.
     * @param administratorDTO the administratorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administratorDTO,
     * or with status {@code 400 (Bad Request)} if the administratorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the administratorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the administratorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/administrators/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AdministratorDTO> partialUpdateAdministrator(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdministratorDTO administratorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Administrator partially : {}, {}", id, administratorDTO);
        if (administratorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administratorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administratorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdministratorDTO> result = administratorService.partialUpdate(administratorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administratorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /administrators} : get all the administrators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administrators in body.
     */
    @GetMapping("/administrators")
    public List<AdministratorDTO> getAllAdministrators() {
        log.debug("REST request to get all Administrators");
        return administratorService.findAll();
    }

    /**
     * {@code GET  /administrators/:id} : get the "id" administrator.
     *
     * @param id the id of the administratorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administratorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/administrators/{id}")
    public ResponseEntity<AdministratorDTO> getAdministrator(@PathVariable Long id) {
        log.debug("REST request to get Administrator : {}", id);
        Optional<AdministratorDTO> administratorDTO = administratorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(administratorDTO);
    }

    /**
     * {@code DELETE  /administrators/:id} : delete the "id" administrator.
     *
     * @param id the id of the administratorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/administrators/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        log.debug("REST request to delete Administrator : {}", id);
        administratorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
