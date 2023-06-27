import feature_extraction.FlowGenerator;
import inet.ipaddr.ipv4.IPv4Address;
import org.junit.jupiter.api.Test;

public class FeatureExtractionApplicationTest {
    @Test
    public void test_ipaddress() {
        IPv4Address address = new IPv4Address(-1);
        System.out.println(address);
    }

    @Test
    public void test_generate_flow() {
        FlowGenerator flowGenerator = new FlowGenerator();
        flowGenerator.generate(10, 1);
    }
}
