package sn.yegg.app.service.dto;

import java.io.Serializable;
import sn.yegg.app.domain.enumeration.AlertStatus;

public class ToggleAlerteLigneArretDTO implements Serializable {

    private Long alerteLigneArretId;
    private AlertStatus alertStatus;

    ToggleAlerteLigneArretDTO() {}

    public ToggleAlerteLigneArretDTO(Long alerteLigneArretId, AlertStatus alertStatus) {
        this.alerteLigneArretId = alerteLigneArretId;
        this.alertStatus = alertStatus;
    }

    public Long getAlerteLigneArretId() {
        return alerteLigneArretId;
    }

    public void setAlerteLigneArretId(Long alerteLigneArretId) {
        this.alerteLigneArretId = alerteLigneArretId;
    }

    public AlertStatus getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(AlertStatus alertStatus) {
        this.alertStatus = alertStatus;
    }
}
