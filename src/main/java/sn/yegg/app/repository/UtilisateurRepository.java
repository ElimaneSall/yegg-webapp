package sn.yegg.app.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.yegg.app.domain.Utilisateur;
import sn.yegg.app.service.dto.UtilisateurDTO;

/**
 * Spring Data JPA repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>, JpaSpecificationExecutor<Utilisateur> {
    @Query("SELECT u FROM Utilisateur u WHERE u.matricule = :matricule OR u.email = :email")
    Optional<Utilisateur> findByMatriculeOrEmail(@Param("matricule") String matricule, @Param("email") String email);
}
