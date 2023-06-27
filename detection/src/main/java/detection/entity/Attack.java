package detection.entity;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Attack {

    private Long id;

    private String srcIp;

    private Instant detectedTime;
}
