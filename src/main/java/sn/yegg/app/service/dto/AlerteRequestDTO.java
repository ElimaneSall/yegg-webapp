package sn.yegg.app.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import sn.yegg.app.domain.enumeration.AlertStatus;
import sn.yegg.app.domain.enumeration.ThresholdType;

public class AlerteRequestDTO implements Serializable {

    // AlerteApproche fields
    private String nom;

    @Min(value = 50)
    @Max(value = 1000)
    private Integer seuilDistance;

    @Min(value = 1)
    @Max(value = 60)
    private Integer seuilTemps;

    @NotNull
    private ThresholdType typeSeuil;

    private String joursActivation;

    private String heureDebut;

    private String heureFin;

    @NotNull
    private AlertStatus statut;

    // AlerteLigneArret fields
    @NotNull
    private Long ligneId;

    @NotNull
    private Long arretId;

    private String sens;

    private Boolean actif;

    // User info
    @NotNull
    private Long utilisateurId;

    // Getters and setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getSeuilDistance() {
        return seuilDistance;
    }

    public void setSeuilDistance(Integer seuilDistance) {
        this.seuilDistance = seuilDistance;
    }

    public Integer getSeuilTemps() {
        return seuilTemps;
    }

    public void setSeuilTemps(Integer seuilTemps) {
        this.seuilTemps = seuilTemps;
    }

    public ThresholdType getTypeSeuil() {
        return typeSeuil;
    }

    public void setTypeSeuil(ThresholdType typeSeuil) {
        this.typeSeuil = typeSeuil;
    }

    public String getJoursActivation() {
        return joursActivation;
    }

    public void setJoursActivation(String joursActivation) {
        this.joursActivation = joursActivation;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public AlertStatus getStatut() {
        return statut;
    }

    public void setStatut(AlertStatus statut) {
        this.statut = statut;
    }

    public Long getLigneId() {
        return ligneId;
    }

    public void setLigneId(Long ligneId) {
        this.ligneId = ligneId;
    }

    public Long getArretId() {
        return arretId;
    }

    public void setArretId(Long arretId) {
        this.arretId = arretId;
    }

    public String getSens() {
        return sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
}
