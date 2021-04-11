package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Merchant;
import fr.polytech.info4.domain.enumeration.MerchantType;
import fr.polytech.info4.repository.MerchantRepository;
import fr.polytech.info4.service.dto.MerchantDTO;
import fr.polytech.info4.service.mapper.MerchantMapper;
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
 * Integration tests for the {@link MerchantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MerchantResourceIT {

    private static final String DEFAULT_MERCHANT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MERCHANT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final MerchantType DEFAULT_MERCHANT_TYPE = MerchantType.RESTAURANT;
    private static final MerchantType UPDATED_MERCHANT_TYPE = MerchantType.CRAFTMAN;

    private static final byte[] DEFAULT_IMAGE_PROFIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_PROFIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_PROFIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_PROFIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/merchants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MerchantRepository merchantRepository;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMerchantMockMvc;

    private Merchant merchant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .merchantName(DEFAULT_MERCHANT_NAME)
            .address(DEFAULT_ADDRESS)
            .merchantType(DEFAULT_MERCHANT_TYPE)
            .imageProfil(DEFAULT_IMAGE_PROFIL)
            .imageProfilContentType(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(DEFAULT_MOBILE_PHONE);
        return merchant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Merchant createUpdatedEntity(EntityManager em) {
        Merchant merchant = new Merchant()
            .merchantName(UPDATED_MERCHANT_NAME)
            .address(UPDATED_ADDRESS)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        return merchant;
    }

    @BeforeEach
    public void initTest() {
        merchant = createEntity(em);
    }

    @Test
    @Transactional
    void createMerchant() throws Exception {
        int databaseSizeBeforeCreate = merchantRepository.findAll().size();
        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);
        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isCreated());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate + 1);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getMerchantName()).isEqualTo(DEFAULT_MERCHANT_NAME);
        assertThat(testMerchant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMerchant.getMerchantType()).isEqualTo(DEFAULT_MERCHANT_TYPE);
        assertThat(testMerchant.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PROFIL);
        assertThat(testMerchant.getImageProfilContentType()).isEqualTo(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testMerchant.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void createMerchantWithExistingId() throws Exception {
        // Create the Merchant with an existing ID
        merchant.setId(1L);
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        int databaseSizeBeforeCreate = merchantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMerchantNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setMerchantName(null);

        // Create the Merchant, which fails.
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setAddress(null);

        // Create the Merchant, which fails.
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMerchantTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setMerchantType(null);

        // Create the Merchant, which fails.
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobilePhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = merchantRepository.findAll().size();
        // set the field null
        merchant.setMobilePhone(null);

        // Create the Merchant, which fails.
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        restMerchantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isBadRequest());

        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMerchants() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get all the merchantList
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(merchant.getId().intValue())))
            .andExpect(jsonPath("$.[*].merchantName").value(hasItem(DEFAULT_MERCHANT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].merchantType").value(hasItem(DEFAULT_MERCHANT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].imageProfilContentType").value(hasItem(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageProfil").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL))))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)));
    }

    @Test
    @Transactional
    void getMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        // Get the merchant
        restMerchantMockMvc
            .perform(get(ENTITY_API_URL_ID, merchant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(merchant.getId().intValue()))
            .andExpect(jsonPath("$.merchantName").value(DEFAULT_MERCHANT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.merchantType").value(DEFAULT_MERCHANT_TYPE.toString()))
            .andExpect(jsonPath("$.imageProfilContentType").value(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageProfil").value(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL)))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingMerchant() throws Exception {
        // Get the merchant
        restMerchantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant
        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).get();
        // Disconnect from session so that the updates on updatedMerchant are not directly saved in db
        em.detach(updatedMerchant);
        updatedMerchant
            .merchantName(UPDATED_MERCHANT_NAME)
            .address(UPDATED_ADDRESS)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        MerchantDTO merchantDTO = merchantMapper.toDto(updatedMerchant);

        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getMerchantName()).isEqualTo(UPDATED_MERCHANT_NAME);
        assertThat(testMerchant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMerchant.getMerchantType()).isEqualTo(UPDATED_MERCHANT_TYPE);
        assertThat(testMerchant.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testMerchant.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testMerchant.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, merchantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(merchantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getMerchantName()).isEqualTo(DEFAULT_MERCHANT_NAME);
        assertThat(testMerchant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMerchant.getMerchantType()).isEqualTo(DEFAULT_MERCHANT_TYPE);
        assertThat(testMerchant.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PROFIL);
        assertThat(testMerchant.getImageProfilContentType()).isEqualTo(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testMerchant.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateMerchantWithPatch() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();

        // Update the merchant using partial update
        Merchant partialUpdatedMerchant = new Merchant();
        partialUpdatedMerchant.setId(merchant.getId());

        partialUpdatedMerchant
            .merchantName(UPDATED_MERCHANT_NAME)
            .address(UPDATED_ADDRESS)
            .merchantType(UPDATED_MERCHANT_TYPE)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMerchant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMerchant))
            )
            .andExpect(status().isOk());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
        Merchant testMerchant = merchantList.get(merchantList.size() - 1);
        assertThat(testMerchant.getMerchantName()).isEqualTo(UPDATED_MERCHANT_NAME);
        assertThat(testMerchant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMerchant.getMerchantType()).isEqualTo(UPDATED_MERCHANT_TYPE);
        assertThat(testMerchant.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testMerchant.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testMerchant.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, merchantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMerchant() throws Exception {
        int databaseSizeBeforeUpdate = merchantRepository.findAll().size();
        merchant.setId(count.incrementAndGet());

        // Create the Merchant
        MerchantDTO merchantDTO = merchantMapper.toDto(merchant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMerchantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(merchantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Merchant in the database
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMerchant() throws Exception {
        // Initialize the database
        merchantRepository.saveAndFlush(merchant);

        int databaseSizeBeforeDelete = merchantRepository.findAll().size();

        // Delete the merchant
        restMerchantMockMvc
            .perform(delete(ENTITY_API_URL_ID, merchant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Merchant> merchantList = merchantRepository.findAll();
        assertThat(merchantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
