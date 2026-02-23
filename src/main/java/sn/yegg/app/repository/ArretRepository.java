package sn.yegg.app.repository;

import java.util.List;
import org.springframework.data.jpa.repository.*;
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
}
