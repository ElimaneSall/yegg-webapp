package sn.yegg.app.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.yegg.app.domain.*;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;
import sn.yegg.app.repository.*;
import sn.yegg.app.service.AlerteApprocheService;
import sn.yegg.app.service.NotificationService;
import sn.yegg.app.service.dto.*;
import sn.yegg.app.service.mapper.AlerteApprocheMapper;
import sn.yegg.app.service.mapper.AlerteLigneArretMapper;
import sn.yegg.app.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link sn.yegg.app.domain.AlerteApproche}.
 */
@Service
@Transactional
public class AlerteApprocheServiceImpl implements AlerteApprocheService {

    private static final Logger LOG = LoggerFactory.getLogger(AlerteApprocheServiceImpl.class);

    private final AlerteApprocheRepository alerteApprocheRepository;

    private final AlerteApprocheMapper alerteApprocheMapper;
    private final AlerteLigneArretRepository alerteLigneArretRepository;
    private final HistoriqueAlerteRepository historiqueAlerteRepository;
    private final LigneRepository ligneRepository;
    private final ArretRepository arretRepository;
    private final UtilisateurRepository utilisateurRepository;

    private final AlerteLigneArretMapper alerteLigneArretMapper;

    public AlerteApprocheServiceImpl(
        AlerteApprocheRepository alerteApprocheRepository,
        AlerteApprocheMapper alerteApprocheMapper,
        AlerteLigneArretRepository alerteLigneArretRepository,
        HistoriqueAlerteRepository historiqueAlerteRepository,
        LigneRepository ligneRepository,
        ArretRepository arretRepository,
        UtilisateurRepository utilisateurRepository,
        AlerteLigneArretMapper alerteLigneArretMapper
    ) {
        this.alerteApprocheRepository = alerteApprocheRepository;
        this.alerteApprocheMapper = alerteApprocheMapper;
        this.alerteLigneArretRepository = alerteLigneArretRepository;
        this.historiqueAlerteRepository = historiqueAlerteRepository;
        this.ligneRepository = ligneRepository;
        this.arretRepository = arretRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.alerteLigneArretMapper = alerteLigneArretMapper;
    }

    @Override
    public AlerteApprocheDTO save(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to save AlerteApproche : {}", alerteApprocheDTO);
        AlerteApproche alerteApproche = alerteApprocheMapper.toEntity(alerteApprocheDTO);
        alerteApproche = alerteApprocheRepository.save(alerteApproche);
        return alerteApprocheMapper.toDto(alerteApproche);
    }

    @Override
    public AlerteApprocheDTO update(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to update AlerteApproche : {}", alerteApprocheDTO);
        AlerteApproche alerteApproche = alerteApprocheMapper.toEntity(alerteApprocheDTO);
        alerteApproche = alerteApprocheRepository.save(alerteApproche);
        return alerteApprocheMapper.toDto(alerteApproche);
    }

    @Override
    public Optional<AlerteApprocheDTO> partialUpdate(AlerteApprocheDTO alerteApprocheDTO) {
        LOG.debug("Request to partially update AlerteApproche : {}", alerteApprocheDTO);

        return alerteApprocheRepository
            .findById(alerteApprocheDTO.getId())
            .map(existingAlerteApproche -> {
                alerteApprocheMapper.partialUpdate(existingAlerteApproche, alerteApprocheDTO);

                return existingAlerteApproche;
            })
            .map(alerteApprocheRepository::save)
            .map(alerteApprocheMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AlerteApprocheDTO> findOne(Long id) {
        LOG.debug("Request to get AlerteApproche : {}", id);
        return alerteApprocheRepository.findById(id).map(alerteApprocheMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete AlerteApproche : {}", id);
        alerteApprocheRepository.deleteById(id);
    }

    @Transactional
    public AlerteResponseDTO storeCompleteAlert(AlerteRequestDTO request) {
        LOG.debug("Request to create complete alerte: {}", request);

        // 1. Find and validate entities
        Utilisateur utilisateur = utilisateurRepository
            .findById(request.getUtilisateurId())
            .orElseThrow(() -> new BadRequestAlertException("Utilisateur not found", "alerteCreation", "usernotfound"));

        Ligne ligne = ligneRepository
            .findById(request.getLigneId())
            .orElseThrow(() -> new BadRequestAlertException("Ligne not found", "alerteCreation", "lignenotfound"));

        Arret arret = arretRepository
            .findById(request.getArretId())
            .orElseThrow(() -> new BadRequestAlertException("Arret not found", "alerteCreation", "arretnotfound"));

        // 2. Create AlerteApproche
        AlerteApproche alerteApproche = new AlerteApproche();
        alerteApproche.setNom(request.getNom());
        alerteApproche.setSeuilDistance(request.getSeuilDistance());
        alerteApproche.setSeuilTemps(request.getSeuilTemps());
        alerteApproche.setTypeSeuil(request.getTypeSeuil());
        alerteApproche.setJoursActivation(request.getJoursActivation());
        alerteApproche.setHeureDebut(request.getHeureDebut());
        alerteApproche.setHeureFin(request.getHeureFin());
        alerteApproche.setStatut(request.getStatut() != null ? request.getStatut() : AlertStatus.ACTIVE);
        alerteApproche.setDateCreation(Instant.now());
        alerteApproche.setNombreDeclenchements(0);
        alerteApproche.setUtilisateur(utilisateur);

        alerteApproche = alerteApprocheRepository.save(alerteApproche);

        // 3. Create AlerteLigneArret
        AlerteLigneArret alerteLigneArret = new AlerteLigneArret();
        alerteLigneArret.setSens(request.getSens());
        alerteLigneArret.setActif(request.getActif() != null ? request.getActif() : true);
        alerteLigneArret.setLigne(ligne);
        alerteLigneArret.setArret(arret);
        alerteLigneArret.setAlerteApproche(alerteApproche);

        alerteLigneArret = alerteLigneArretRepository.save(alerteLigneArret);

        // 4. Convert to DTOs and return
        AlerteApprocheDTO alerteApprocheDTO = alerteApprocheMapper.toDto(alerteApproche);
        AlerteLigneArretDTO alerteLigneArretDTO = alerteLigneArretMapper.toDto(alerteLigneArret);

        return new AlerteResponseDTO(alerteApprocheDTO, alerteLigneArretDTO);
    }

    public AlerteResponseDTO toggleCompleteAlert(ToggleAlerteLigneArretDTO toggleAlerteLigneArretDTO) {
        // Vérification de l'existence
        AlerteLigneArret alerteLigneArret = alerteLigneArretRepository
            .findById(toggleAlerteLigneArretDTO.getAlerteLigneArretId())
            .orElseThrow(() ->
                new EntityNotFoundException("AlerteLigneArret introuvable avec l'id " + toggleAlerteLigneArretDTO.getAlerteLigneArretId())
            );

        AlerteApproche alerteApproche = alerteLigneArret.getAlerteApproche();

        if (AlertStatus.ACTIVE.equals(alerteApproche.getStatut())) {
            alerteApproche.setStatut(AlertStatus.DISABLED);
            alerteLigneArret.setActif(false);
        } else {
            alerteApproche.setStatut(AlertStatus.ACTIVE);
            alerteLigneArret.setActif(true);
        }
        alerteApprocheRepository.save(alerteApproche);

        alerteLigneArretRepository.save(alerteLigneArret);

        AlerteResponseDTO alerteResponseDTO = new AlerteResponseDTO();
        alerteResponseDTO.setAlerteApproche(alerteApprocheMapper.toDto(alerteApproche));
        alerteResponseDTO.setAlerteLigneArret(alerteLigneArretMapper.toDto(alerteLigneArret));
        // Préparation de la réponse
        return alerteResponseDTO;
    }

    @Transactional
    public void deleteCompleteAlert(Long alerteLigneArretId) {
        AlerteLigneArret alerteLigneArret = alerteLigneArretRepository
            .findById(alerteLigneArretId)
            .orElseThrow(() -> new EntityNotFoundException("AlerteLigneArret introuvable avec l'id " + alerteLigneArretId));

        AlerteApproche alerteApproche = alerteLigneArret.getAlerteApproche();

        if (alerteApproche != null) {
            // supprimer historique
            historiqueAlerteRepository.deleteByAlerteApprocheId(alerteApproche.getId());

            // supprimer alerte approche
            alerteApprocheRepository.delete(alerteApproche);
        }

        // supprimer relation ligne/arret
        alerteLigneArretRepository.delete(alerteLigneArret);

        LOG.debug("Alerte complète supprimée pour alerteLigneArretId={}", alerteLigneArretId);
    }
}
