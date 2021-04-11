package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.AdministratorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrator} and its DTO {@link AdministratorDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface AdministratorMapper extends EntityMapper<AdministratorDTO, Administrator> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    AdministratorDTO toDto(Administrator s);
}
