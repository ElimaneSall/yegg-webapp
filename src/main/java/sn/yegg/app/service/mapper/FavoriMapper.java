package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Favori;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.FavoriDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Favori} and its DTO {@link FavoriDTO}.
 */
@Mapper(componentModel = "spring")
public interface FavoriMapper extends EntityMapper<FavoriDTO, Favori> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    FavoriDTO toDto(Favori s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
