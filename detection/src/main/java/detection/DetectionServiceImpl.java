package detection;

import detection_interface.DetectionService;
import detection_interface.FlowInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@DubboService
@Component
public class DetectionServiceImpl implements DetectionService {

    private final Logger logger = LoggerFactory.getLogger(DetectionServiceImpl.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private Queue queue;

    @Override
    public void detectFlow(FlowInfo flowInfo) {
        logger.info("sent: " + flowInfo);
        template.convertAndSend(queue.getName(), flowInfo);
    }
}
