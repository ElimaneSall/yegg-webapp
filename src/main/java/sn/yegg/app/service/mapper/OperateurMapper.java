package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Operateur;
import sn.yegg.app.service.dto.OperateurDTO;

/**
 * Mapper for the entity {@link Operateur} and its DTO {@link OperateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface OperateurMapper extends EntityMapper<OperateurDTO, Operateur> {}
