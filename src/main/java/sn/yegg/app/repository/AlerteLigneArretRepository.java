package sn.yegg.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.AlerteLigneArret;

/**
 * Spring Data JPA repository for the AlerteLigneArret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteLigneArretRepository extends JpaRepository<AlerteLigneArret, Long>, JpaSpecificationExecutor<AlerteLigneArret> {
    List<AlerteLigneArret> findByAlerteApprocheId(Long alerteApprocheId);
    List<AlerteLigneArret> findByLigneIdAndActifTrue(Long ligneId);
}
