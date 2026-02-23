package sn.yegg.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.AlerteApproche;
import sn.yegg.app.domain.enumeration.AlertStatus;

/**
 * Spring Data JPA repository for the AlerteApproche entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteApprocheRepository extends JpaRepository<AlerteApproche, Long>, JpaSpecificationExecutor<AlerteApproche> {
    List<AlerteApproche> findByUtilisateurId(Long utilisateurId);

    List<AlerteApproche> findByStatut(AlertStatus statut);

    default List<AlerteApproche> findActiveAlerts() {
        return findByStatut(AlertStatus.ACTIVE);
    }
}
