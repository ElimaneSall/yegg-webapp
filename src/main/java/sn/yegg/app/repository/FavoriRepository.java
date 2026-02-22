package sn.yegg.app.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Favori;

/**
 * Spring Data JPA repository for the Favori entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FavoriRepository extends JpaRepository<Favori, Long>, JpaSpecificationExecutor<Favori> {}
