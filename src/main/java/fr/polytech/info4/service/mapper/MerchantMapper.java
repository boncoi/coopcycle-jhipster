package fr.polytech.info4.service.mapper;

import fr.polytech.info4.domain.*;
import fr.polytech.info4.service.dto.MerchantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Merchant} and its DTO {@link MerchantDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, CooperativeMapper.class })
public interface MerchantMapper extends EntityMapper<MerchantDTO, Merchant> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "cooperative", source = "cooperative", qualifiedByName = "cooperativeName")
    MerchantDTO toDto(Merchant s);

    @Named("merchantName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "merchantName", source = "merchantName")
    MerchantDTO toDtoMerchantName(Merchant merchant);
}
