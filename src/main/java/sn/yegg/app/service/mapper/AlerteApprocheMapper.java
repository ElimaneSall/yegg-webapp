package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.AlerteApprocheDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link AlerteApproche} and its DTO {@link AlerteApprocheDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlerteApprocheMapper extends EntityMapper<AlerteApprocheDTO, AlerteApproche> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    AlerteApprocheDTO toDto(AlerteApproche s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
