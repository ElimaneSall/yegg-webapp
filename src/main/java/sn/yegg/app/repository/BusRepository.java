package sn.yegg.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Bus;

/**
 * Spring Data JPA repository for the Bus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BusRepository extends JpaRepository<Bus, Long>, JpaSpecificationExecutor<Bus> {
    @Query("SELECT b FROM Bus b LEFT JOIN FETCH b.ligne WHERE b.gpsDeviceId = :deviceId")
    Optional<Bus> findByGpsDeviceIdWithLigne(@Param("deviceId") String deviceId);
}
