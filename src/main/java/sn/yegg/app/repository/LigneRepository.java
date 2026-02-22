package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Ligne;

/**
 * Spring Data JPA repository for the Ligne entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LigneRepository extends JpaRepository<Ligne, Long>, JpaSpecificationExecutor<Ligne> {}
