package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.LigneArret;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.service.dto.LigneArretDTO;
import sn.yegg.app.service.dto.LigneDTO;

/**
 * Mapper for the entity {@link LigneArret} and its DTO {@link LigneArretDTO}.
 */
@Mapper(componentModel = "spring")
public interface LigneArretMapper extends EntityMapper<LigneArretDTO, LigneArret> {
    @Mapping(target = "ligne", source = "ligne", qualifiedByName = "ligneId")
    @Mapping(target = "arret", source = "arret", qualifiedByName = "arretId")
    LigneArretDTO toDto(LigneArret s);

    @Named("ligneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LigneDTO toDtoLigneId(Ligne ligne);

    @Named("arretId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArretDTO toDtoArretId(Arret arret);
}
