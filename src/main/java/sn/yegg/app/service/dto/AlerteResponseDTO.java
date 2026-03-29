package sn.yegg.app.service.dto;

import java.io.Serializable;

public class AlerteResponseDTO implements Serializable {

    private AlerteApprocheDTO alerteApproche;
    private AlerteLigneArretDTO alerteLigneArret;

    public AlerteResponseDTO() {}

    public AlerteResponseDTO(AlerteApprocheDTO alerteApproche, AlerteLigneArretDTO alerteLigneArret) {
        this.alerteApproche = alerteApproche;
        this.alerteLigneArret = alerteLigneArret;
    }

    // Getters and setters
    public AlerteApprocheDTO getAlerteApproche() {
        return alerteApproche;
    }

    public void setAlerteApproche(AlerteApprocheDTO alerteApproche) {
        this.alerteApproche = alerteApproche;
    }

    public AlerteLigneArretDTO getAlerteLigneArret() {
        return alerteLigneArret;
    }

    public void setAlerteLigneArret(AlerteLigneArretDTO alerteLigneArret) {
        this.alerteLigneArret = alerteLigneArret;
    }
}
