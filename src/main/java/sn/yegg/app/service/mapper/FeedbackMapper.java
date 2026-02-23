package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Feedback;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.FeedbackDTO;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Mapper for the entity {@link Feedback} and its DTO {@link FeedbackDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeedbackMapper extends EntityMapper<FeedbackDTO, Feedback> {
    @Mapping(target = "utilisateur", source = "utilisateur", qualifiedByName = "utilisateurId")
    FeedbackDTO toDto(Feedback s);

    @Named("utilisateurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtilisateurDTO toDtoUtilisateurId(Utilisateur utilisateur);
}
