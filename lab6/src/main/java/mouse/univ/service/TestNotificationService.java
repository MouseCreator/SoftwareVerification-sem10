package mouse.univ.service;

import mouse.univ.model.Notification;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;


public class TestNotificationService implements NotificationService {

    private final List<Notification> notificationList;

    public TestNotificationService() {
        notificationList = new ArrayList<>();
    }

    @Override
    public void notify(Notification notification) {
        this.notificationList.add(notification);
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notificationList);
    }


}
