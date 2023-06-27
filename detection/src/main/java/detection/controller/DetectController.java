package detection.controller;

import detection.service.DetectService;
import detection_interface.FlowInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class DetectController {
    @Autowired
    private DetectService detectService;

    @RabbitListener(queues = "flow-info")
    public void detect(FlowInfo flowInfo) {
        detectService.detect(flowInfo);
    }
}
