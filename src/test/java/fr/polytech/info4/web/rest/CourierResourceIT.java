package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Courier;
import fr.polytech.info4.repository.CourierRepository;
import fr.polytech.info4.service.dto.CourierDTO;
import fr.polytech.info4.service.mapper.CourierMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CourierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourierResourceIT {

    private static final Boolean DEFAULT_WORKING = false;
    private static final Boolean UPDATED_WORKING = true;

    private static final byte[] DEFAULT_IMAGE_PROFIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_PROFIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_PROFIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_PROFIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/couriers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private CourierMapper courierMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourierMockMvc;

    private Courier courier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courier createEntity(EntityManager em) {
        Courier courier = new Courier()
            .working(DEFAULT_WORKING)
            .imageProfil(DEFAULT_IMAGE_PROFIL)
            .imageProfilContentType(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(DEFAULT_MOBILE_PHONE);
        return courier;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Courier createUpdatedEntity(EntityManager em) {
        Courier courier = new Courier()
            .working(UPDATED_WORKING)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        return courier;
    }

    @BeforeEach
    public void initTest() {
        courier = createEntity(em);
    }

    @Test
    @Transactional
    void createCourier() throws Exception {
        int databaseSizeBeforeCreate = courierRepository.findAll().size();
        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);
        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isCreated());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeCreate + 1);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getWorking()).isEqualTo(DEFAULT_WORKING);
        assertThat(testCourier.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PROFIL);
        assertThat(testCourier.getImageProfilContentType()).isEqualTo(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testCourier.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void createCourierWithExistingId() throws Exception {
        // Create the Courier with an existing ID
        courier.setId(1L);
        CourierDTO courierDTO = courierMapper.toDto(courier);

        int databaseSizeBeforeCreate = courierRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWorkingIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierRepository.findAll().size();
        // set the field null
        courier.setWorking(null);

        // Create the Courier, which fails.
        CourierDTO courierDTO = courierMapper.toDto(courier);

        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobilePhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = courierRepository.findAll().size();
        // set the field null
        courier.setMobilePhone(null);

        // Create the Courier, which fails.
        CourierDTO courierDTO = courierMapper.toDto(courier);

        restCourierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isBadRequest());

        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCouriers() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        // Get all the courierList
        restCourierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courier.getId().intValue())))
            .andExpect(jsonPath("$.[*].working").value(hasItem(DEFAULT_WORKING.booleanValue())))
            .andExpect(jsonPath("$.[*].imageProfilContentType").value(hasItem(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageProfil").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL))))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)));
    }

    @Test
    @Transactional
    void getCourier() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        // Get the courier
        restCourierMockMvc
            .perform(get(ENTITY_API_URL_ID, courier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courier.getId().intValue()))
            .andExpect(jsonPath("$.working").value(DEFAULT_WORKING.booleanValue()))
            .andExpect(jsonPath("$.imageProfilContentType").value(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageProfil").value(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL)))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingCourier() throws Exception {
        // Get the courier
        restCourierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCourier() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier
        Courier updatedCourier = courierRepository.findById(courier.getId()).get();
        // Disconnect from session so that the updates on updatedCourier are not directly saved in db
        em.detach(updatedCourier);
        updatedCourier
            .working(UPDATED_WORKING)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        CourierDTO courierDTO = courierMapper.toDto(updatedCourier);

        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getWorking()).isEqualTo(UPDATED_WORKING);
        assertThat(testCourier.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testCourier.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testCourier.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(courierDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourierWithPatch() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier using partial update
        Courier partialUpdatedCourier = new Courier();
        partialUpdatedCourier.setId(courier.getId());

        partialUpdatedCourier
            .working(UPDATED_WORKING)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);

        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourier))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getWorking()).isEqualTo(UPDATED_WORKING);
        assertThat(testCourier.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testCourier.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testCourier.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateCourierWithPatch() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeUpdate = courierRepository.findAll().size();

        // Update the courier using partial update
        Courier partialUpdatedCourier = new Courier();
        partialUpdatedCourier.setId(courier.getId());

        partialUpdatedCourier
            .working(UPDATED_WORKING)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourier.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCourier))
            )
            .andExpect(status().isOk());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
        Courier testCourier = courierList.get(courierList.size() - 1);
        assertThat(testCourier.getWorking()).isEqualTo(UPDATED_WORKING);
        assertThat(testCourier.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testCourier.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testCourier.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courierDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourier() throws Exception {
        int databaseSizeBeforeUpdate = courierRepository.findAll().size();
        courier.setId(count.incrementAndGet());

        // Create the Courier
        CourierDTO courierDTO = courierMapper.toDto(courier);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourierMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(courierDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Courier in the database
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourier() throws Exception {
        // Initialize the database
        courierRepository.saveAndFlush(courier);

        int databaseSizeBeforeDelete = courierRepository.findAll().size();

        // Delete the courier
        restCourierMockMvc
            .perform(delete(ENTITY_API_URL_ID, courier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Courier> courierList = courierRepository.findAll();
        assertThat(courierList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
