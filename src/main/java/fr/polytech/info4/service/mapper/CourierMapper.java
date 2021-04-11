package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.CourierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Courier} and its DTO {@link CourierDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CooperativeMapper.class })
public interface CourierMapper extends EntityMapper<CourierDTO, Courier> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    CourierDTO toDto(Courier s);
}
