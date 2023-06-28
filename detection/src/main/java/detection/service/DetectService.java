package detection.service;

import detection.dao.AttackDao;
import detection.dao.FlowInfoDao;
import detection.entity.Attack;
import detection_interface.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
public class DetectService {

    @SuppressWarnings("FieldCanBeLocal")
    private final int PACKETS_THRESHOLD = 0;

    @SuppressWarnings("FieldCanBeLocal")
    private final int ALERTS_THRESHOLD = 5;

    @Autowired
    private FlowInfoDao flowInfoDao;

    @Autowired
    private AttackDao attackDao;

    @Autowired
    private AlertCountService alertCountService;

    @RabbitListener(queues = "flow-info")
    public void detect(FlowInfo flowInfo) {
        log.info("detect: " + flowInfo + "'");

        flowInfoDao.insert(flowInfo);

        if (flowInfo.getForwardPackets() > PACKETS_THRESHOLD) {
            log.info("alert: " + flowInfo.getSrcIp());

            long alerts = alertCountService.alertCount(flowInfo);
            log.info("num of alert: " + alerts);

            if (alerts > ALERTS_THRESHOLD) {
                Attack attack = Attack.builder()
                        .srcIp(flowInfo.getSrcIp())
                        .detectedTime(Instant.now())
                        .build();

                attackDao.insert(attack);
                if (attack.getId() != null) {
                    log.info("[x] insert attack: " + attack);
                } else {
                    log.info("attack from " + attack.getSrcIp() + " has already been logged.");
                }
            }
        }
    }
}
