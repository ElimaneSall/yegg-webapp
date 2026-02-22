package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.LigneArret;

/**
 * Spring Data JPA repository for the LigneArret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneArretRepository extends JpaRepository<LigneArret, Long>, JpaSpecificationExecutor<LigneArret> {}
