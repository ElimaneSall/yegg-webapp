package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.dto.LigneDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Bus} and its DTO {@link BusDTO}.
 */
@Mapper(componentModel = "spring")
public interface BusMapper extends EntityMapper<BusDTO, Bus> {
    @Mapping(target = "ligne", source = "ligne", qualifiedByName = "ligneId")
    @Mapping(target = "chauffeur", source = "chauffeur", qualifiedByName = "utilisateurId")
    BusDTO toDto(Bus s);

    @Named("ligneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LigneDTO toDtoLigneId(Ligne ligne);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
