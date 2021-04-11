package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.ConsumerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Consumer} and its DTO {@link ConsumerDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface ConsumerMapper extends EntityMapper<ConsumerDTO, Consumer> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    ConsumerDTO toDto(Consumer s);
}
