package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.HistoriqueAlerte;

/**
 * Spring Data JPA repository for the HistoriqueAlerte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoriqueAlerteRepository extends JpaRepository<HistoriqueAlerte, Long>, JpaSpecificationExecutor<HistoriqueAlerte> {
    @Modifying
    @Query("delete from HistoriqueAlerte h where h.alerteApproche.id = :id")
    void deleteByAlerteApprocheId(@Param("id") Long id);
}
