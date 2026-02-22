package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.service.dto.OperateurDTO;

/**
 * Mapper for the entity {@link Ligne} and its DTO {@link LigneDTO}.
 */
@Mapper(componentModel = "spring")
public interface LigneMapper extends EntityMapper<LigneDTO, Ligne> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "operateurId")
    LigneDTO toDto(Ligne s);

    @Named("operateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OperateurDTO toDtoOperateurId(Operateur operateur);
}
