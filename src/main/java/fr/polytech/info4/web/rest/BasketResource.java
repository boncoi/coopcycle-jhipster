package fr.polytech.info4.web.rest;

import fr.polytech.info4.repository.BasketRepository;
import fr.polytech.info4.service.BasketService;
import fr.polytech.info4.service.dto.BasketDTO;
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
 * REST controller for managing {@link fr.polytech.info4.domain.Basket}.
 */
@RestController
@RequestMapping("/api")
public class BasketResource {

    private final Logger log = LoggerFactory.getLogger(BasketResource.class);

    private static final String ENTITY_NAME = "basket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BasketService basketService;

    private final BasketRepository basketRepository;

    public BasketResource(BasketService basketService, BasketRepository basketRepository) {
        this.basketService = basketService;
        this.basketRepository = basketRepository;
    }

    /**
     * {@code POST  /baskets} : Create a new basket.
     *
     * @param basketDTO the basketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new basketDTO, or with status {@code 400 (Bad Request)} if the basket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/baskets")
    public ResponseEntity<BasketDTO> createBasket(@Valid @RequestBody BasketDTO basketDTO) throws URISyntaxException {
        log.debug("REST request to save Basket : {}", basketDTO);
        if (basketDTO.getId() != null) {
            throw new BadRequestAlertException("A new basket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasketDTO result = basketService.save(basketDTO);
        return ResponseEntity
            .created(new URI("/api/baskets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /baskets/:id} : Updates an existing basket.
     *
     * @param id the id of the basketDTO to save.
     * @param basketDTO the basketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketDTO,
     * or with status {@code 400 (Bad Request)} if the basketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the basketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/baskets/{id}")
    public ResponseEntity<BasketDTO> updateBasket(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BasketDTO basketDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Basket : {}, {}", id, basketDTO);
        if (basketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BasketDTO result = basketService.save(basketDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /baskets/:id} : Partial updates given fields of an existing basket, field will ignore if it is null
     *
     * @param id the id of the basketDTO to save.
     * @param basketDTO the basketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated basketDTO,
     * or with status {@code 400 (Bad Request)} if the basketDTO is not valid,
     * or with status {@code 404 (Not Found)} if the basketDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the basketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/baskets/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<BasketDTO> partialUpdateBasket(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BasketDTO basketDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Basket partially : {}, {}", id, basketDTO);
        if (basketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, basketDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!basketRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BasketDTO> result = basketService.partialUpdate(basketDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, basketDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /baskets} : get all the baskets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of baskets in body.
     */
    @GetMapping("/baskets")
    public List<BasketDTO> getAllBaskets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Baskets");
        return basketService.findAll();
    }

    /**
     * {@code GET  /baskets/:id} : get the "id" basket.
     *
     * @param id the id of the basketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the basketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/baskets/{id}")
    public ResponseEntity<BasketDTO> getBasket(@PathVariable Long id) {
        log.debug("REST request to get Basket : {}", id);
        Optional<BasketDTO> basketDTO = basketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(basketDTO);
    }

    /**
     * {@code DELETE  /baskets/:id} : delete the "id" basket.
     *
     * @param id the id of the basketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/baskets/{id}")
    public ResponseEntity<Void> deleteBasket(@PathVariable Long id) {
        log.debug("REST request to delete Basket : {}", id);
        basketService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
