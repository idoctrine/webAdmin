package com.webadmin.example.service;

import com.webadmin.example.bean.MailBox;
import com.webadmin.example.bean.PopularMailBox;
import com.webadmin.example.bean.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparingInt;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private static final int MAX_POPULAR_MAIL_BOXES = 3;

    private final UserWebClient userWebClient;

    public List<PopularMailBox> popularMailboxes() {
        Map<String, List<String>> map = listingUserMailboxes();

        return map
                .entrySet()
                .stream()
                .sorted(reverseOrder(comparingInt(p -> p.getValue().size())))
                .limit(MAX_POPULAR_MAIL_BOXES)
                .map(p -> new PopularMailBox(p.getKey(), p.getValue().size()))
                .toList();
    }

    public Map<String, List<String>> listingUserMailboxes() {
        Map<String, List<String>> map = new HashMap<>();

        List<User> users  = userWebClient.retrievingTheUserList();
        users.forEach(user-> {
            List<MailBox> mailBoxes = userWebClient.retrievingMailboxesByUser(user);
            mailBoxes.forEach(mailBox ->
                    map.computeIfAbsent(mailBox.mailboxName(), m -> new ArrayList<>()).add(user.username())
            );
        });

        return map;
    }
}
