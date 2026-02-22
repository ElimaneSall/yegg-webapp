package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Alerte;

/**
 * Spring Data JPA repository for the Alerte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AlerteRepository extends JpaRepository<Alerte, Long>, JpaSpecificationExecutor<Alerte> {}
