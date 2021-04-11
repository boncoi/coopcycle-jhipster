package fr.polytech.info4.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.polytech.info4.IntegrationTest;
import fr.polytech.info4.domain.Administrator;
import fr.polytech.info4.repository.AdministratorRepository;
import fr.polytech.info4.service.dto.AdministratorDTO;
import fr.polytech.info4.service.mapper.AdministratorMapper;
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
 * Integration tests for the {@link AdministratorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdministratorResourceIT {

    private static final Integer DEFAULT_LEVEL_OF_AUTHORITY = 1;
    private static final Integer UPDATED_LEVEL_OF_AUTHORITY = 2;

    private static final byte[] DEFAULT_IMAGE_PROFIL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_PROFIL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_PROFIL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_PROFIL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_MOBILE_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE_PHONE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/administrators";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private AdministratorMapper administratorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministratorMockMvc;

    private Administrator administrator;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrator createEntity(EntityManager em) {
        Administrator administrator = new Administrator()
            .levelOfAuthority(DEFAULT_LEVEL_OF_AUTHORITY)
            .imageProfil(DEFAULT_IMAGE_PROFIL)
            .imageProfilContentType(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(DEFAULT_MOBILE_PHONE);
        return administrator;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrator createUpdatedEntity(EntityManager em) {
        Administrator administrator = new Administrator()
            .levelOfAuthority(UPDATED_LEVEL_OF_AUTHORITY)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        return administrator;
    }

    @BeforeEach
    public void initTest() {
        administrator = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrator() throws Exception {
        int databaseSizeBeforeCreate = administratorRepository.findAll().size();
        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);
        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeCreate + 1);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getLevelOfAuthority()).isEqualTo(DEFAULT_LEVEL_OF_AUTHORITY);
        assertThat(testAdministrator.getImageProfil()).isEqualTo(DEFAULT_IMAGE_PROFIL);
        assertThat(testAdministrator.getImageProfilContentType()).isEqualTo(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testAdministrator.getMobilePhone()).isEqualTo(DEFAULT_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void createAdministratorWithExistingId() throws Exception {
        // Create the Administrator with an existing ID
        administrator.setId(1L);
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        int databaseSizeBeforeCreate = administratorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLevelOfAuthorityIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setLevelOfAuthority(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMobilePhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = administratorRepository.findAll().size();
        // set the field null
        administrator.setMobilePhone(null);

        // Create the Administrator, which fails.
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        restAdministratorMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministrators() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get all the administratorList
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrator.getId().intValue())))
            .andExpect(jsonPath("$.[*].levelOfAuthority").value(hasItem(DEFAULT_LEVEL_OF_AUTHORITY)))
            .andExpect(jsonPath("$.[*].imageProfilContentType").value(hasItem(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].imageProfil").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL))))
            .andExpect(jsonPath("$.[*].mobilePhone").value(hasItem(DEFAULT_MOBILE_PHONE)));
    }

    @Test
    @Transactional
    void getAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        // Get the administrator
        restAdministratorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrator.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrator.getId().intValue()))
            .andExpect(jsonPath("$.levelOfAuthority").value(DEFAULT_LEVEL_OF_AUTHORITY))
            .andExpect(jsonPath("$.imageProfilContentType").value(DEFAULT_IMAGE_PROFIL_CONTENT_TYPE))
            .andExpect(jsonPath("$.imageProfil").value(Base64Utils.encodeToString(DEFAULT_IMAGE_PROFIL)))
            .andExpect(jsonPath("$.mobilePhone").value(DEFAULT_MOBILE_PHONE));
    }

    @Test
    @Transactional
    void getNonExistingAdministrator() throws Exception {
        // Get the administrator
        restAdministratorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator
        Administrator updatedAdministrator = administratorRepository.findById(administrator.getId()).get();
        // Disconnect from session so that the updates on updatedAdministrator are not directly saved in db
        em.detach(updatedAdministrator);
        updatedAdministrator
            .levelOfAuthority(UPDATED_LEVEL_OF_AUTHORITY)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);
        AdministratorDTO administratorDTO = administratorMapper.toDto(updatedAdministrator);

        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getLevelOfAuthority()).isEqualTo(UPDATED_LEVEL_OF_AUTHORITY);
        assertThat(testAdministrator.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testAdministrator.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testAdministrator.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void putNonExistingAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministratorWithPatch() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator using partial update
        Administrator partialUpdatedAdministrator = new Administrator();
        partialUpdatedAdministrator.setId(administrator.getId());

        partialUpdatedAdministrator
            .levelOfAuthority(UPDATED_LEVEL_OF_AUTHORITY)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrator))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getLevelOfAuthority()).isEqualTo(UPDATED_LEVEL_OF_AUTHORITY);
        assertThat(testAdministrator.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testAdministrator.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testAdministrator.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void fullUpdateAdministratorWithPatch() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();

        // Update the administrator using partial update
        Administrator partialUpdatedAdministrator = new Administrator();
        partialUpdatedAdministrator.setId(administrator.getId());

        partialUpdatedAdministrator
            .levelOfAuthority(UPDATED_LEVEL_OF_AUTHORITY)
            .imageProfil(UPDATED_IMAGE_PROFIL)
            .imageProfilContentType(UPDATED_IMAGE_PROFIL_CONTENT_TYPE)
            .mobilePhone(UPDATED_MOBILE_PHONE);

        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrator.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrator))
            )
            .andExpect(status().isOk());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
        Administrator testAdministrator = administratorList.get(administratorList.size() - 1);
        assertThat(testAdministrator.getLevelOfAuthority()).isEqualTo(UPDATED_LEVEL_OF_AUTHORITY);
        assertThat(testAdministrator.getImageProfil()).isEqualTo(UPDATED_IMAGE_PROFIL);
        assertThat(testAdministrator.getImageProfilContentType()).isEqualTo(UPDATED_IMAGE_PROFIL_CONTENT_TYPE);
        assertThat(testAdministrator.getMobilePhone()).isEqualTo(UPDATED_MOBILE_PHONE);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administratorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrator() throws Exception {
        int databaseSizeBeforeUpdate = administratorRepository.findAll().size();
        administrator.setId(count.incrementAndGet());

        // Create the Administrator
        AdministratorDTO administratorDTO = administratorMapper.toDto(administrator);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministratorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administratorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrator in the database
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrator() throws Exception {
        // Initialize the database
        administratorRepository.saveAndFlush(administrator);

        int databaseSizeBeforeDelete = administratorRepository.findAll().size();

        // Delete the administrator
        restAdministratorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrator.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrator> administratorList = administratorRepository.findAll();
        assertThat(administratorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
