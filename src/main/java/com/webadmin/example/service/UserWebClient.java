package com.webadmin.example.service;

import com.webadmin.example.bean.MailBox;
import com.webadmin.example.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Component
@Slf4j
public class UserWebClient {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private static final String URL_RETRIEVING_USER_LIST = "/users";

    private static final String LISTING_USER_MAILBOXES = "/mailboxes";

    @Value( "${webAdmin.url}" )
    private String webAdminUrl;

    public List<MailBox> retrievingMailboxesByUser(User user) {
        try {
            ResponseEntity<List<MailBox>> responseEntity = WebClient
                    .create()
                    .get()
                    .uri(webAdminUrl + "/" + user.username() + LISTING_USER_MAILBOXES)
                    .retrieve()
                    .toEntityList(MailBox.class)
                    .block(TIMEOUT);
            if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                log.error("Le service retourne une réponse null ou un utilisateur qui n'existe pas");
                return List.of();
            }
        } catch (Exception e) {
            log.error("Le service ne répond pas : " + e.getMessage(), e);
            return List.of();
        }
    }

    public List<User> retrievingTheUserList() {
        try {
            ResponseEntity<List<User>> responseEntity = WebClient
                    .create()
                    .get()
                    .uri(webAdminUrl + URL_RETRIEVING_USER_LIST)
                    .retrieve()
                    .toEntityList(User.class)
                    .block(TIMEOUT);
            if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                log.error("Le service retourne une réponse null ou non valide");
                return List.of();
            }
        } catch (Exception e) {
            log.error("Le service ne répond pas : " + e.getMessage(), e);
            return List.of();
        }
    }
}
