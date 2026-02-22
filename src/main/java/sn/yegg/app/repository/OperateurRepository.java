package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Operateur;

/**
 * Spring Data JPA repository for the Operateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OperateurRepository extends JpaRepository<Operateur, Long>, JpaSpecificationExecutor<Operateur> {}
