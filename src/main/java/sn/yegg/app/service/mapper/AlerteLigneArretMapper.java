package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.AlerteLigneArret;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.domain.Ligne;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.dto.AlerteLigneArretDTO;
import sn.yegg.app.service.dto.ArretDTO;
import sn.yegg.app.service.dto.LigneDTO;

/**
 * Mapper for the entity {@link AlerteLigneArret} and its DTO {@link AlerteLigneArretDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlerteLigneArretMapper extends EntityMapper<AlerteLigneArretDTO, AlerteLigneArret> {
    @Mapping(target = "ligne", source = "ligne", qualifiedByName = "ligneId")
    @Mapping(target = "arret", source = "arret", qualifiedByName = "arretId")
    @Mapping(target = "alerteApproche", source = "alerteApproche", qualifiedByName = "alerteApprocheId")
    AlerteLigneArretDTO toDto(AlerteLigneArret s);

    @Named("ligneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "numero", source = "numero")
    LigneDTO toDtoLigneId(Ligne ligne);

    @Named("arretId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "altitude", source = "altitude")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "ville", source = "ville")
    ArretDTO toDtoArretId(Arret arret);

    @Named("alerteApprocheId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "seuilDistance", source = "seuilDistance")
    @Mapping(target = "seuilTemps", source = "seuilTemps")
    @Mapping(target = "typeSeuil", source = "typeSeuil")
    @Mapping(target = "joursActivation", source = "joursActivation")
    @Mapping(target = "heureDebut", source = "heureDebut")
    @Mapping(target = "heureFin", source = "heureFin")
    @Mapping(target = "statut", source = "statut")
    @Mapping(target = "dateCreation", source = "dateCreation")
    @Mapping(target = "dateModification", source = "dateModification")
    @Mapping(target = "dernierDeclenchement", source = "dernierDeclenchement")
    @Mapping(target = "nombreDeclenchements", source = "nombreDeclenchements")
    AlerteApprocheDTO toDtoAlerteApprocheId(AlerteApproche alerteApproche);
}
