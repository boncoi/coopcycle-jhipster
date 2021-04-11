package fr.polytech.info4.service;

import fr.polytech.info4.domain.Administrator;
import fr.polytech.info4.repository.AdministratorRepository;
import fr.polytech.info4.service.dto.AdministratorDTO;
import fr.polytech.info4.service.mapper.AdministratorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Administrator}.
 */
@Service
@Transactional
public class AdministratorService {

    private final Logger log = LoggerFactory.getLogger(AdministratorService.class);

    private final AdministratorRepository administratorRepository;

    private final AdministratorMapper administratorMapper;

    public AdministratorService(AdministratorRepository administratorRepository, AdministratorMapper administratorMapper) {
        this.administratorRepository = administratorRepository;
        this.administratorMapper = administratorMapper;
    }

    /**
     * Save a administrator.
     *
     * @param administratorDTO the entity to save.
     * @return the persisted entity.
     */
    public AdministratorDTO save(AdministratorDTO administratorDTO) {
        log.debug("Request to save Administrator : {}", administratorDTO);
        Administrator administrator = administratorMapper.toEntity(administratorDTO);
        administrator = administratorRepository.save(administrator);
        return administratorMapper.toDto(administrator);
    }

    /**
     * Partially update a administrator.
     *
     * @param administratorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdministratorDTO> partialUpdate(AdministratorDTO administratorDTO) {
        log.debug("Request to partially update Administrator : {}", administratorDTO);

        return administratorRepository
            .findById(administratorDTO.getId())
            .map(
                existingAdministrator -> {
                    administratorMapper.partialUpdate(existingAdministrator, administratorDTO);
                    return existingAdministrator;
                }
            )
            .map(administratorRepository::save)
            .map(administratorMapper::toDto);
    }

    /**
     * Get all the administrators.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AdministratorDTO> findAll() {
        log.debug("Request to get all Administrators");
        return administratorRepository.findAll().stream().map(administratorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one administrator by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdministratorDTO> findOne(Long id) {
        log.debug("Request to get Administrator : {}", id);
        return administratorRepository.findById(id).map(administratorMapper::toDto);
    }

    /**
     * Delete the administrator by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Administrator : {}", id);
        administratorRepository.deleteById(id);
    }
}
