package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.MerchantRepository;
import fr.polytech.info4.service.MerchantService;
import fr.polytech.info4.service.dto.MerchantDTO;
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
 * REST controller for managing {@link fr.polytech.info4.domain.Merchant}.
 */
@RestController
@RequestMapping("/api")
public class MerchantResource {

    private final Logger log = LoggerFactory.getLogger(MerchantResource.class);

    private static final String ENTITY_NAME = "merchant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MerchantService merchantService;

    private final MerchantRepository merchantRepository;

    public MerchantResource(MerchantService merchantService, MerchantRepository merchantRepository) {
        this.merchantService = merchantService;
        this.merchantRepository = merchantRepository;
    }

    /**
     * {@code POST  /merchants} : Create a new merchant.
     *
     * @param merchantDTO the merchantDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchantDTO, or with status {@code 400 (Bad Request)} if the merchant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/merchants")
    public ResponseEntity<MerchantDTO> createMerchant(@Valid @RequestBody MerchantDTO merchantDTO) throws URISyntaxException {
        log.debug("REST request to save Merchant : {}", merchantDTO);
        if (merchantDTO.getId() != null) {
            throw new BadRequestAlertException("A new merchant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MerchantDTO result = merchantService.save(merchantDTO);
        return ResponseEntity
            .created(new URI("/api/merchants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /merchants/:id} : Updates an existing merchant.
     *
     * @param id the id of the merchantDTO to save.
     * @param merchantDTO the merchantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantDTO,
     * or with status {@code 400 (Bad Request)} if the merchantDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/merchants/{id}")
    public ResponseEntity<MerchantDTO> updateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MerchantDTO merchantDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Merchant : {}, {}", id, merchantDTO);
        if (merchantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MerchantDTO result = merchantService.save(merchantDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /merchants/:id} : Partial updates given fields of an existing merchant, field will ignore if it is null
     *
     * @param id the id of the merchantDTO to save.
     * @param merchantDTO the merchantDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantDTO,
     * or with status {@code 400 (Bad Request)} if the merchantDTO is not valid,
     * or with status {@code 404 (Not Found)} if the merchantDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the merchantDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/merchants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MerchantDTO> partialUpdateMerchant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MerchantDTO merchantDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Merchant partially : {}, {}", id, merchantDTO);
        if (merchantDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, merchantDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!merchantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MerchantDTO> result = merchantService.partialUpdate(merchantDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, merchantDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /merchants} : get all the merchants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchants in body.
     */
    @GetMapping("/merchants")
    public List<MerchantDTO> getAllMerchants() {
        log.debug("REST request to get all Merchants");
        return merchantService.findAll();
    }

    /**
     * {@code GET  /merchants/:id} : get the "id" merchant.
     *
     * @param id the id of the merchantDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchantDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/merchants/{id}")
    public ResponseEntity<MerchantDTO> getMerchant(@PathVariable Long id) {
        log.debug("REST request to get Merchant : {}", id);
        Optional<MerchantDTO> merchantDTO = merchantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(merchantDTO);
    }

    /**
     * {@code DELETE  /merchants/:id} : delete the "id" merchant.
     *
     * @param id the id of the merchantDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/merchants/{id}")
    public ResponseEntity<Void> deleteMerchant(@PathVariable Long id) {
        log.debug("REST request to delete Merchant : {}", id);
        merchantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
