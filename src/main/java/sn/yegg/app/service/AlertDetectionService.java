package sn.yegg.app.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sn.yegg.app.domain.Bus;

@Service
@Transactional
public interface AlertDetectionService {
    void checkApproachAlerts(Bus bus);
}
