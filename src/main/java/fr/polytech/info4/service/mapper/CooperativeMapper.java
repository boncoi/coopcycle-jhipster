package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.CooperativeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cooperative} and its DTO {@link CooperativeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CooperativeMapper extends EntityMapper<CooperativeDTO, Cooperative> {
    @Named("cooperativeName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "cooperativeName", source = "cooperativeName")
    CooperativeDTO toDtoCooperativeName(Cooperative cooperative);
}
