package fr.polytech.info4.service;

import fr.polytech.info4.domain.Basket;
import fr.polytech.info4.repository.BasketRepository;
import fr.polytech.info4.service.dto.BasketDTO;
import fr.polytech.info4.service.mapper.BasketMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Basket}.
 */
@Service
@Transactional
public class BasketService {

    private final Logger log = LoggerFactory.getLogger(BasketService.class);

    private final BasketRepository basketRepository;

    private final BasketMapper basketMapper;

    public BasketService(BasketRepository basketRepository, BasketMapper basketMapper) {
        this.basketRepository = basketRepository;
        this.basketMapper = basketMapper;
    }

    /**
     * Save a basket.
     *
     * @param basketDTO the entity to save.
     * @return the persisted entity.
     */
    public BasketDTO save(BasketDTO basketDTO) {
        log.debug("Request to save Basket : {}", basketDTO);
        Basket basket = basketMapper.toEntity(basketDTO);
        basket = basketRepository.save(basket);
        return basketMapper.toDto(basket);
    }

    /**
     * Partially update a basket.
     *
     * @param basketDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BasketDTO> partialUpdate(BasketDTO basketDTO) {
        log.debug("Request to partially update Basket : {}", basketDTO);

        return basketRepository
            .findById(basketDTO.getId())
            .map(
                existingBasket -> {
                    basketMapper.partialUpdate(existingBasket, basketDTO);
                    return existingBasket;
                }
            )
            .map(basketRepository::save)
            .map(basketMapper::toDto);
    }

    /**
     * Get all the baskets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BasketDTO> findAll() {
        log.debug("Request to get all Baskets");
        return basketRepository
            .findAllWithEagerRelationships()
            .stream()
            .map(basketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the baskets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<BasketDTO> findAllWithEagerRelationships(Pageable pageable) {
        return basketRepository.findAllWithEagerRelationships(pageable).map(basketMapper::toDto);
    }

    /**
     * Get one basket by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BasketDTO> findOne(Long id) {
        log.debug("Request to get Basket : {}", id);
        return basketRepository.findOneWithEagerRelationships(id).map(basketMapper::toDto);
    }

    /**
     * Delete the basket by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Basket : {}", id);
        basketRepository.deleteById(id);
    }
}
