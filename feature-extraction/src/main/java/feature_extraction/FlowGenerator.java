package feature_extraction;

import detection_interface.DetectionService;
import detection_interface.FiveTuple;
import detection_interface.FlowInfo;
import inet.ipaddr.ipv4.IPv4Address;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Slf4j
@Data
public class FlowGenerator {

    private static final int MAX_PORT_NUMBER = 65535;

    private int minFlowDuration = 10;

    private int maxFlowDuration = 10000;

    private int minPacketsPerSecond = 1;

    private int maxPacketsPerSecond = 100;

    private int minBytesPerPacket = 1;

    private int maxBytesPerPacket = 100;

    @DubboReference
    private DetectionService detectionService;

    protected static FiveTuple[] generateFiveTuples(int num, @SuppressWarnings("SameParameterValue") int protocol) {
        FiveTuple[] fiveTuples = new FiveTuple[num];

        Random random = ThreadLocalRandom.current();
        int[] srcIPNumbers = random.ints(Integer.MIN_VALUE, Integer.MAX_VALUE).distinct().limit(num).toArray();
        int[] dstIPNumbers = random.ints(Integer.MIN_VALUE, Integer.MAX_VALUE).distinct().limit(num).toArray();

        for (int i = 0; i < num; i++) {
            int srcIPNumber = srcIPNumbers[i];
            IPv4Address srcAddress = new IPv4Address(srcIPNumber);
            String srcIP = srcAddress.toString();

            int dstIPNumber = dstIPNumbers[i];
            IPv4Address dstAddress = new IPv4Address(dstIPNumber);
            String dstIP = dstAddress.toString();

            int srcPort = random.nextInt(0, MAX_PORT_NUMBER);
            int dstPort = random.nextInt(0, MAX_PORT_NUMBER);

            FiveTuple fiveTuple = new FiveTuple(srcIP, srcPort, dstIP, dstPort, protocol);
            fiveTuples[i] = fiveTuple;
        }

        return fiveTuples;
    }

    public void generate(int num, double frequence) throws InterruptedException {
        Random random = ThreadLocalRandom.current();

        FiveTuple[] fiveTuples = generateFiveTuples(num, 17);

        double meanInterval = 1 / (frequence * num);
        double minInterval = 0;
        double maxInterval = 2 * meanInterval;

        //noinspection InfiniteLoopStatement
        while (true) {
            for (int i = 0; i < num; i++) {
                FiveTuple fiveTuple = fiveTuples[i];
                int duration = random.nextInt(minFlowDuration, maxFlowDuration);
                int packetsPerSecond = random.nextInt(minPacketsPerSecond, maxPacketsPerSecond);
                int bytesPerPacket = random.nextInt(minBytesPerPacket, maxBytesPerPacket);
                int bytesPerSecond = bytesPerPacket * packetsPerSecond;

                FlowInfo flowInfo = FlowInfo.builder()
                        .timestamp(Instant.now())
                        .srcIp(fiveTuple.srcIp())
                        .srcPort(fiveTuple.srcPort())
                        .dstIp(fiveTuple.dstIp())
                        .dstPort(fiveTuple.dstPort())
                        .protocol(fiveTuple.protocol())
                        .flowDuration(duration)
                        .forwardPackets(packetsPerSecond)
                        .forwardBytes(bytesPerSecond)
                        .backwardPackets(packetsPerSecond)
                        .backwardBytes(bytesPerSecond)
                        .build();

                log.info(flowInfo.toString());
                detectionService.detectFlow(flowInfo);

                int interval = (int) (random.nextDouble(minInterval, maxInterval) * 1000);
                //noinspection BusyWait
                Thread.sleep(interval);
            }
        }
    }
}
