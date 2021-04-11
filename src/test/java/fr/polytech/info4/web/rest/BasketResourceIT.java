package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Basket;
import fr.polytech.info4.repository.BasketRepository;
import fr.polytech.info4.service.BasketService;
import fr.polytech.info4.service.dto.BasketDTO;
import fr.polytech.info4.service.mapper.BasketMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BasketResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BasketResourceIT {

    private static final UUID DEFAULT_ID_BASKET = UUID.randomUUID();
    private static final UUID UPDATED_ID_BASKET = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/baskets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BasketRepository basketRepository;

    @Mock
    private BasketRepository basketRepositoryMock;

    @Autowired
    private BasketMapper basketMapper;

    @Mock
    private BasketService basketServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBasketMockMvc;

    private Basket basket;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Basket createEntity(EntityManager em) {
        Basket basket = new Basket().idBasket(DEFAULT_ID_BASKET);
        return basket;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Basket createUpdatedEntity(EntityManager em) {
        Basket basket = new Basket().idBasket(UPDATED_ID_BASKET);
        return basket;
    }

    @BeforeEach
    public void initTest() {
        basket = createEntity(em);
    }

    @Test
    @Transactional
    void createBasket() throws Exception {
        int databaseSizeBeforeCreate = basketRepository.findAll().size();
        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);
        restBasketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basketDTO)))
            .andExpect(status().isCreated());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeCreate + 1);
        Basket testBasket = basketList.get(basketList.size() - 1);
        assertThat(testBasket.getIdBasket()).isEqualTo(DEFAULT_ID_BASKET);
    }

    @Test
    @Transactional
    void createBasketWithExistingId() throws Exception {
        // Create the Basket with an existing ID
        basket.setId(1L);
        BasketDTO basketDTO = basketMapper.toDto(basket);

        int databaseSizeBeforeCreate = basketRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBasketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basketDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIdBasketIsRequired() throws Exception {
        int databaseSizeBeforeTest = basketRepository.findAll().size();
        // set the field null
        basket.setIdBasket(null);

        // Create the Basket, which fails.
        BasketDTO basketDTO = basketMapper.toDto(basket);

        restBasketMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basketDTO)))
            .andExpect(status().isBadRequest());

        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBaskets() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        // Get all the basketList
        restBasketMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(basket.getId().intValue())))
            .andExpect(jsonPath("$.[*].idBasket").value(hasItem(DEFAULT_ID_BASKET.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBasketsWithEagerRelationshipsIsEnabled() throws Exception {
        when(basketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBasketMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(basketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBasketsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(basketServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBasketMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(basketServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBasket() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        // Get the basket
        restBasketMockMvc
            .perform(get(ENTITY_API_URL_ID, basket.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(basket.getId().intValue()))
            .andExpect(jsonPath("$.idBasket").value(DEFAULT_ID_BASKET.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBasket() throws Exception {
        // Get the basket
        restBasketMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBasket() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        int databaseSizeBeforeUpdate = basketRepository.findAll().size();

        // Update the basket
        Basket updatedBasket = basketRepository.findById(basket.getId()).get();
        // Disconnect from session so that the updates on updatedBasket are not directly saved in db
        em.detach(updatedBasket);
        updatedBasket.idBasket(UPDATED_ID_BASKET);
        BasketDTO basketDTO = basketMapper.toDto(updatedBasket);

        restBasketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isOk());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
        Basket testBasket = basketList.get(basketList.size() - 1);
        assertThat(testBasket.getIdBasket()).isEqualTo(UPDATED_ID_BASKET);
    }

    @Test
    @Transactional
    void putNonExistingBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, basketDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(basketDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBasketWithPatch() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        int databaseSizeBeforeUpdate = basketRepository.findAll().size();

        // Update the basket using partial update
        Basket partialUpdatedBasket = new Basket();
        partialUpdatedBasket.setId(basket.getId());

        partialUpdatedBasket.idBasket(UPDATED_ID_BASKET);

        restBasketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasket))
            )
            .andExpect(status().isOk());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
        Basket testBasket = basketList.get(basketList.size() - 1);
        assertThat(testBasket.getIdBasket()).isEqualTo(UPDATED_ID_BASKET);
    }

    @Test
    @Transactional
    void fullUpdateBasketWithPatch() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        int databaseSizeBeforeUpdate = basketRepository.findAll().size();

        // Update the basket using partial update
        Basket partialUpdatedBasket = new Basket();
        partialUpdatedBasket.setId(basket.getId());

        partialUpdatedBasket.idBasket(UPDATED_ID_BASKET);

        restBasketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBasket.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBasket))
            )
            .andExpect(status().isOk());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
        Basket testBasket = basketList.get(basketList.size() - 1);
        assertThat(testBasket.getIdBasket()).isEqualTo(UPDATED_ID_BASKET);
    }

    @Test
    @Transactional
    void patchNonExistingBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, basketDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBasket() throws Exception {
        int databaseSizeBeforeUpdate = basketRepository.findAll().size();
        basket.setId(count.incrementAndGet());

        // Create the Basket
        BasketDTO basketDTO = basketMapper.toDto(basket);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBasketMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(basketDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Basket in the database
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBasket() throws Exception {
        // Initialize the database
        basketRepository.saveAndFlush(basket);

        int databaseSizeBeforeDelete = basketRepository.findAll().size();

        // Delete the basket
        restBasketMockMvc
            .perform(delete(ENTITY_API_URL_ID, basket.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Basket> basketList = basketRepository.findAll();
        assertThat(basketList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
