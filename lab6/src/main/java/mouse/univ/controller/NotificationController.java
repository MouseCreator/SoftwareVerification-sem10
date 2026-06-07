package mouse.univ.controller;

import lombok.extern.log4j.Log4j2;
import mouse.univ.model.Notification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Log4j2
public class NotificationController {

    @PostMapping("/nofify")
    void postNotification(@RequestBody Notification notification) {
       log.info("Received notification: {}", notification.getMessage());
    }
}
