package sn.yegg.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.AlerteLigneArret;

/**
 * Spring Data JPA repository for the AlerteLigneArret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteLigneArretRepository extends JpaRepository<AlerteLigneArret, Long>, JpaSpecificationExecutor<AlerteLigneArret> {
    List<AlerteLigneArret> findByAlerteApprocheId(Long alerteApprocheId);

    List<AlerteLigneArret> findByLigneId(Long ligneId);

    List<AlerteLigneArret> findByLigneIdAndActifTrue(Long ligneId);

    @Query(
        "SELECT ala FROM AlerteLigneArret ala " +
        "JOIN FETCH ala.alerteApproche aa " +
        "JOIN FETCH aa.utilisateur u " +
        "JOIN FETCH ala.ligne l " +
        "JOIN FETCH ala.arret a " +
        "WHERE l.id = :ligneId " +
        "AND aa.statut = 'ACTIVE' " +
        "AND ala.actif = true"
    )
    List<AlerteLigneArret> findActiveAlertsWithUsersByLigne(@Param("ligneId") Long ligneId);
}
