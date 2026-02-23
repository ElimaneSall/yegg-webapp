package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.domain.Rapport;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.OperateurDTO;
import sn.yegg.app.service.dto.RapportDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Rapport} and its DTO {@link RapportDTO}.
 */
@Mapper(componentModel = "spring")
public interface RapportMapper extends EntityMapper<RapportDTO, Rapport> {
    @Mapping(target = "operateur", source = "operateur", qualifiedByName = "operateurId")
    @Mapping(target = "admin", source = "admin", qualifiedByName = "utilisateurId")
    RapportDTO toDto(Rapport s);

    @Named("operateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OperateurDTO toDtoOperateurId(Operateur operateur);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
