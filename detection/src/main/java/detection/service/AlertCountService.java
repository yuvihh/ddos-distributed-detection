package detection.service;

import detection_interface.FlowInfo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AlertCountService {

    private final Duration DURATION = Duration.ofSeconds(5);

    @SuppressWarnings("FieldCanBeLocal")
    private final int MAX_NUM_ALERT = 5;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    public boolean alertCount(FlowInfo flowInfo) {
        String key = getKey(flowInfo);
        Instant timestamp = flowInfo.getTimestamp();
        Instant cutoffTimestamp = timestamp.minus(DURATION);
        zSetOperations.removeRangeByScore(key, 0, cutoffTimestamp.getEpochSecond());
        Long count = zSetOperations.size(key);
        if (count != null) return count > MAX_NUM_ALERT;
        else throw new RuntimeException("count is null");
    }

    protected String getKey(FlowInfo flowInfo) {
        String srcIp = flowInfo.getSrcIp();
        Integer srcPort = flowInfo.getSrcPort();
        return "alert:" + srcIp + ":" + srcPort;
    }
}
