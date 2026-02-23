package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Rapport;

/**
 * Spring Data JPA repository for the Rapport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RapportRepository extends JpaRepository<Rapport, Long>, JpaSpecificationExecutor<Rapport> {}
