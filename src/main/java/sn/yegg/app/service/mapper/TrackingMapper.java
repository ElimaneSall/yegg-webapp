package sn.yegg.app.service.mapper;

import org.mapstruct.*;
import sn.yegg.app.domain.Bus;
import sn.yegg.app.domain.Tracking;
import sn.yegg.app.service.dto.BusDTO;
import sn.yegg.app.service.dto.TrackingDTO;

/**
 * Mapper for the entity {@link Tracking} and its DTO {@link TrackingDTO}.
 */
@Mapper(componentModel = "spring")
public interface TrackingMapper extends EntityMapper<TrackingDTO, Tracking> {
    @Mapping(target = "bus", source = "bus", qualifiedByName = "busId")
    TrackingDTO toDto(Tracking s);

    @Named("busId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BusDTO toDtoBusId(Bus bus);
}
