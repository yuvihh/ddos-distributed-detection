package detection;

import detection_interface.DetectionService;
import detection_interface.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DubboService
@Component
@Slf4j
public class DetectionServiceImpl implements DetectionService {

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    @Override
    public void detectFlow(FlowInfo flowInfo) {
        log.info("sent: " + flowInfo);
        template.convertAndSend(queue.getName(), flowInfo);
    }
}
