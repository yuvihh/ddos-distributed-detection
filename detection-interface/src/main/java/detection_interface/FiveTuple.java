package detection_interface;

public record FiveTuple(String srcIp,
                        int srcPort,
                        String dstIp,
                        int dstPort,
                        int protocol) {}
