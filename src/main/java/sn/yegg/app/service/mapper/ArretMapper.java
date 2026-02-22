package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Arret;
import sn.yegg.app.service.dto.ArretDTO;

/**
 * Mapper for the entity {@link Arret} and its DTO {@link ArretDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArretMapper extends EntityMapper<ArretDTO, Arret> {}
