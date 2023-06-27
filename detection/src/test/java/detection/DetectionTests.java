package detection;

import detection.dao.AttackDao;
import detection.dao.FlowInfoDao;
import detection.entity.Attack;
import detection_interface.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest
@Slf4j
public class DetectionTests {

    @Autowired
    private FlowInfoDao flowInfoDao;

    @Autowired
    private AttackDao attackDao;

    @Test
    public void test_insertFlowInfo() {
        FlowInfo flowInfo = FlowInfo.builder()
                .timestamp(Instant.now())
                .srcIp("192.168.1.5")
                .srcPort(2341)
                .dstIp("192.168.2.3")
                .dstPort(80)
                .protocol(6)
                .flowDuration(300)
                .forwardPackets(23)
                .forwardBytes(32)
                .backwardPackets(32)
                .backwardBytes(32)
                .build();
        flowInfoDao.insert(flowInfo);
        log.info("[x] insert: " + flowInfo);
    }

    @Test
    public void test_insertAttack() {
        Attack attack = Attack.builder()
                .srcIp("192.168.1.3")
                .detectedTime(Instant.now())
                .build();
        attackDao.insert(attack);
        log.info("[x] insert attack: " + attack);
    }
}
