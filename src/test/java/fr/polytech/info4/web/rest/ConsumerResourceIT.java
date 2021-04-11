package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Consumer;
import fr.polytech.info4.repository.ConsumerRepository;
import fr.polytech.info4.service.dto.ConsumerDTO;
import fr.polytech.info4.service.mapper.ConsumerMapper;
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
 * Integration tests for the {@link ConsumerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConsumerResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE_PROFIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_PROFIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_PROFIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_PROFIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/consumers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ConsumerMapper consumerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConsumerMockMvc;

    private Consumer consumer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .address(DEFAULT_ADDRESS)
            .imageProfil(DEFAULT_IMAGE_PROFIL)
            .imageProfilContentType(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(DEFAULT_MOBILE_PHONE);
        return consumer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumer createUpdatedEntity(EntityManager em) {
        Consumer consumer = new Consumer()
            .address(UPDATED_ADDRESS)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        return consumer;
    }

    @BeforeEach
    public void initTest() {
        consumer = createEntity(em);
    }

    @Test
    @Transactional
    void createConsumer() throws Exception {
        int databaseSizeBeforeCreate = consumerRepository.findAll().size();
        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);
        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isCreated());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate + 1);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testConsumer.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PROFIL);
        assertThat(testConsumer.getImageProfilContentType()).isEqualTo(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testConsumer.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void createConsumerWithExistingId() throws Exception {
        // Create the Consumer with an existing ID
        consumer.setId(1L);
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        int databaseSizeBeforeCreate = consumerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerRepository.findAll().size();
        // set the field null
        consumer.setAddress(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobilePhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = consumerRepository.findAll().size();
        // set the field null
        consumer.setMobilePhone(null);

        // Create the Consumer, which fails.
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        restConsumerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isBadRequest());

        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConsumers() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get all the consumerList
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumer.getId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].imageProfilContentType").value(hasItem(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageProfil").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL))))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)));
    }

    @Test
    @Transactional
    void getConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        // Get the consumer
        restConsumerMockMvc
            .perform(get(ENTITY_API_URL_ID, consumer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(consumer.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.imageProfilContentType").value(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageProfil").value(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL)))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingConsumer() throws Exception {
        // Get the consumer
        restConsumerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer
        Consumer updatedConsumer = consumerRepository.findById(consumer.getId()).get();
        // Disconnect from session so that the updates on updatedConsumer are not directly saved in db
        em.detach(updatedConsumer);
        updatedConsumer
            .address(UPDATED_ADDRESS)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        ConsumerDTO consumerDTO = consumerMapper.toDto(updatedConsumer);

        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testConsumer.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testConsumer.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testConsumer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(consumerDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        partialUpdatedConsumer
            .address(UPDATED_ADDRESS)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testConsumer.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testConsumer.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testConsumer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateConsumerWithPatch() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();

        // Update the consumer using partial update
        Consumer partialUpdatedConsumer = new Consumer();
        partialUpdatedConsumer.setId(consumer.getId());

        partialUpdatedConsumer
            .address(UPDATED_ADDRESS)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConsumer.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConsumer))
            )
            .andExpect(status().isOk());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
        Consumer testConsumer = consumerList.get(consumerList.size() - 1);
        assertThat(testConsumer.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testConsumer.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testConsumer.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testConsumer.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, consumerDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConsumer() throws Exception {
        int databaseSizeBeforeUpdate = consumerRepository.findAll().size();
        consumer.setId(count.incrementAndGet());

        // Create the Consumer
        ConsumerDTO consumerDTO = consumerMapper.toDto(consumer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConsumerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(consumerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Consumer in the database
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConsumer() throws Exception {
        // Initialize the database
        consumerRepository.saveAndFlush(consumer);

        int databaseSizeBeforeDelete = consumerRepository.findAll().size();

        // Delete the consumer
        restConsumerMockMvc
            .perform(delete(ENTITY_API_URL_ID, consumer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consumer> consumerList = consumerRepository.findAll();
        assertThat(consumerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
