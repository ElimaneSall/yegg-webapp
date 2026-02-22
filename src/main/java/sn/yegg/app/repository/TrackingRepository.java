package sn.yegg.app.repository;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Tracking;

/**
 * Spring Data JPA repository for the Tracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long>, JpaSpecificationExecutor<Tracking> {
    List<Tracking> findByBusIdOrderByTimestampDesc(Long busId);

    List<Tracking> findByBusIdAndTimestampAfter(Long busId, Instant timestamp);

    /* @Query("SELECT t FROM Tracking t WHERE t.bus.id = :busId AND t. BETWEEN :start AND :end ORDER BY t.timestamp")
    List<Tracking> findBusPositionsBetween(@Param("busId") Long busId,
                                           @Param("start") Instant start,
                                           @Param("end") Instant end);*/

    @Modifying
    @Transactional
    @Query("DELETE FROM Tracking t WHERE t.timestamp < :cutoffDate")
    void deleteOlderThan(@Param("cutoffDate") Instant cutoffDate);

    @Query("SELECT t FROM Tracking t WHERE t.timestamp > :since ORDER BY t.timestamp DESC")
    List<Tracking> findAllRecentPositions(@Param("since") Instant since);

    @Modifying
    @Transactional
    @Query("DELETE FROM Tracking t WHERE t.timestamp < :cutoffDate")
    int deleteByTimestampBefore(@Param("cutoffDate") Instant cutoffDate);
}
