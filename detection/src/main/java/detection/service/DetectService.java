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
    private final int PACKETS_THRESHOLD = 90;

    @Autowired
    private FlowInfoDao flowInfoDao;

    @Autowired
    private AlertCountService alertCountService;

    @Autowired
    private AttackDao attackDao;

    @RabbitListener(queues = "flow-info")
    public void detect(FlowInfo flowInfo) {
        log.info("[x] receive '" + flowInfo + "'");

        flowInfoDao.insert(flowInfo);
        log.info("[x] insert: " + flowInfo);

        boolean alert = flowInfo.getForwardPackets() > PACKETS_THRESHOLD;
        log.info("[x] detect " + alert);

        if (alert) {
            boolean isAttack = alertCountService.alertCount(flowInfo);
            if (isAttack) {
                Attack attack = Attack.builder()
                        .srcIp(flowInfo.getSrcIp())
                        .detectedTime(Instant.now())
                        .build();
                attackDao.insert(attack);
            }
        }




    }
}
