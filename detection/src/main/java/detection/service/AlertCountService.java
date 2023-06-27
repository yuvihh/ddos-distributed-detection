package detection.service;

import detection_interface.FlowInfo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@Slf4j
public class AlertCountService {

    private final Duration DURATION = Duration.ofSeconds(10);

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    public long alertCount(FlowInfo flowInfo) {
        String key = getKey(flowInfo);
        Instant timestamp = flowInfo.getTimestamp();
        Instant cutoffTimestamp = timestamp.minus(DURATION);
        log.info(cutoffTimestamp.toString());
        zSetOperations.removeRangeByScore(key, 0, cutoffTimestamp.getEpochSecond());
        zSetOperations.add(key, flowInfo.getId().toString(), timestamp.getEpochSecond());
        Long count = zSetOperations.size(key);
        assert count != null;
        return count;
    }

    protected String getKey(FlowInfo flowInfo) {
        String srcIp = flowInfo.getSrcIp();
        Integer srcPort = flowInfo.getSrcPort();
        return "alert:" + srcIp + ":" + srcPort;
    }
}
