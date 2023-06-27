package detection_interface;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder
public class FlowInfo implements Serializable {

    private Long id;

    private Instant timestamp;

    private String srcIp;

    private Integer srcPort;

    private String dstIp;

    private Integer dstPort;

    private Integer protocol;

    private Integer flowDuration;

    private Integer forwardPackets;

    private Integer forwardBytes;

    private Integer backwardPackets;

    private Integer backwardBytes;
}
