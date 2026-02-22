package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Alerte;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.AlerteDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Alerte} and its DTO {@link AlerteDTO}.
 */
@Mapper(componentModel = "spring")
public interface AlerteMapper extends EntityMapper<AlerteDTO, Alerte> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    AlerteDTO toDto(Alerte s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
