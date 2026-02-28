package sn.yegg.app.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Arret;

/**
 * Spring Data JPA repository for the Arret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArretRepository extends JpaRepository<Arret, Long>, JpaSpecificationExecutor<Arret> {
    @Query("SELECT a FROM Arret a " + "JOIN LigneArret la ON la.arret.id = a.id " + "WHERE la.ligne.id = :ligneId " + "ORDER BY la.ordre")
    List<Arret> findByLigneIdOrderByLigneArretOrdre(Long ligneId);

    @Query(
        value = "SELECT *, " +
        "(6371 * acos(cos(radians(:lat)) * cos(radians(a.latitude)) * " +
        "cos(radians(a.longitude) - radians(:lng)) + sin(radians(:lat)) * " +
        "sin(radians(a.latitude)))) AS distance " +
        "FROM arret a " +
        "WHERE a.actif = true " +
        "HAVING distance < :radius " +
        "ORDER BY distance ASC",
        nativeQuery = true
    )
    List<Arret> findNearbyArrets(@Param("lat") Double lat, @Param("lng") Double lng, @Param("radius") Double radius);
}
