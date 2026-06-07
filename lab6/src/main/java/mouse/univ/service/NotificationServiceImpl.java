package mouse.univ.service;

import mouse.univ.model.Notification;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final RestClient restClient;

    public NotificationServiceImpl() {
        restClient = RestClient.builder()
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .baseUrl("http://localhost:8080/nofify")
                .build();
    }
    @Override
    public void notify(Notification notification) {
        restClient.post().body(notification);
    }
}
