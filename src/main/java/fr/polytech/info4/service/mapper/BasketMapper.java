package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.BasketDTO;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Basket} and its DTO {@link BasketDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ProductMapper.class })
public interface BasketMapper extends EntityMapper<BasketDTO, Basket> {
    @Mapping(target = "user", source = "user", qualifiedByName = "id")
    @Mapping(target = "idProducts", source = "idProducts", qualifiedByName = "productIDSet")
    BasketDTO toDto(Basket s);

    @Mapping(target = "removeIdProduct", ignore = true)
    Basket toEntity(BasketDTO basketDTO);

    @Named("idBasket")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "idBasket", source = "idBasket")
    BasketDTO toDtoIdBasket(Basket basket);
}
