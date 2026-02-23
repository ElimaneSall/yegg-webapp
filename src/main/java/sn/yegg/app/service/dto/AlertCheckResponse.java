package sn.yegg.app.service.dto;

import java.time.Instant;
import java.util.List;

public class AlertCheckResponse {

    private boolean alertTriggered;
    private List<TriggeredAlert> triggeredAlerts;
    private Instant checkTimestamp;

    // Getters et Setters
    public boolean isAlertTriggered() {
        return alertTriggered;
    }

    public void setAlertTriggered(boolean alertTriggered) {
        this.alertTriggered = alertTriggered;
    }

    public List<TriggeredAlert> getTriggeredAlerts() {
        return triggeredAlerts;
    }

    public void setTriggeredAlerts(List<TriggeredAlert> triggeredAlerts) {
        this.triggeredAlerts = triggeredAlerts;
    }

    public Instant getCheckTimestamp() {
        return checkTimestamp;
    }

    public void setCheckTimestamp(Instant checkTimestamp) {
        this.checkTimestamp = checkTimestamp;
    }

    public static class TriggeredAlert {

        private Long alertId;
        private String alertName;
        private String thresholdType;
        private Double distanceValue;
        private Integer timeValue;
        private String triggeredBy;
        private Instant triggeredAt;

        // Getters et Setters
        public Long getAlertId() {
            return alertId;
        }

        public void setAlertId(Long alertId) {
            this.alertId = alertId;
        }

        public String getAlertName() {
            return alertName;
        }

        public void setAlertName(String alertName) {
            this.alertName = alertName;
        }

        public String getThresholdType() {
            return thresholdType;
        }

        public void setThresholdType(String thresholdType) {
            this.thresholdType = thresholdType;
        }

        public Double getDistanceValue() {
            return distanceValue;
        }

        public void setDistanceValue(Double distanceValue) {
            this.distanceValue = distanceValue;
        }

        public Integer getTimeValue() {
            return timeValue;
        }

        public void setTimeValue(Integer timeValue) {
            this.timeValue = timeValue;
        }

        public String getTriggeredBy() {
            return triggeredBy;
        }

        public void setTriggeredBy(String triggeredBy) {
            this.triggeredBy = triggeredBy;
        }

        public Instant getTriggeredAt() {
            return triggeredAt;
        }

        public void setTriggeredAt(Instant triggeredAt) {
            this.triggeredAt = triggeredAt;
        }
    }
}
