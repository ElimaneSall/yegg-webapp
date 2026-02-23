package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.HistoriqueAlerte;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.dto.HistoriqueAlerteDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link HistoriqueAlerte} and its DTO {@link HistoriqueAlerteDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoriqueAlerteMapper extends EntityMapper<HistoriqueAlerteDTO, HistoriqueAlerte> {
    @Mapping(target = "bus", source = "bus", qualifiedByName = "busId")
    @Mapping(target = "alerteApproche", source = "alerteApproche", qualifiedByName = "alerteApprocheId")
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    HistoriqueAlerteDTO toDto(HistoriqueAlerte s);

    @Named("busId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BusDTO toDtoBusId(Bus bus);

    @Named("alerteApprocheId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AlerteApprocheDTO toDtoAlerteApprocheId(AlerteApproche alerteApproche);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
