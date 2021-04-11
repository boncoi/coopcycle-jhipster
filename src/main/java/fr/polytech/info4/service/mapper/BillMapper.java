package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.BillDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring", uses = { BasketMapper.class })
public interface BillMapper extends EntityMapper<BillDTO, Bill> {
    @Mapping(target = "idBasket", source = "idBasket", qualifiedByName = "idBasket")
    BillDTO toDto(Bill s);
}
