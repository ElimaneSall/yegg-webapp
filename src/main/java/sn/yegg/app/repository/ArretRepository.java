package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Arret;

/**
 * Spring Data JPA repository for the Arret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArretRepository extends JpaRepository<Arret, Long>, JpaSpecificationExecutor<Arret> {}
