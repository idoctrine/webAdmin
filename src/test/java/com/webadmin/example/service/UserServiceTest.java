package com.webadmin.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webadmin.example.bean.MailBox;
import com.webadmin.example.bean.PopularMailBox;
import com.webadmin.example.bean.User;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserWebClient userWebClient;

    @Test
    void popularMailboxes_should_be_return_report_of_three_most_popular_mailboxes() throws IOException {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();

        String strUsers = IOUtils.toString(getClass().getResourceAsStream("/users.json"));
        String strMailBoxUser1 = IOUtils.toString(getClass().getResourceAsStream("/mailboxes_user1.json"));
        String strMailBoxUser2 = IOUtils.toString(getClass().getResourceAsStream("/mailboxes_user2.json"));
        String strMailBoxUser3 = IOUtils.toString(getClass().getResourceAsStream("/mailboxes_user3.json"));

        List<User> users = Arrays.asList(objectMapper.readValue(strUsers, User[].class));
        List<MailBox> mailBoxsUser1 = Arrays.asList(objectMapper.readValue(strMailBoxUser1, MailBox[].class));
        List<MailBox> mailBoxsUser2 = Arrays.asList(objectMapper.readValue(strMailBoxUser2, MailBox[].class));
        List<MailBox> mailBoxsUser3 = Arrays.asList(objectMapper.readValue(strMailBoxUser3, MailBox[].class));

        when(userWebClient.retrievingTheUserList()).thenReturn(users);
        when(userWebClient.retrievingMailboxesByUser(users.get(0))).thenReturn(mailBoxsUser1);
        when(userWebClient.retrievingMailboxesByUser(users.get(1))).thenReturn(mailBoxsUser2);
        when(userWebClient.retrievingMailboxesByUser(users.get(2))).thenReturn(mailBoxsUser3);

        // when
        List<PopularMailBox> popularMailBox =  userService.popularMailboxes();

        // Then
        assertThat(popularMailBox).hasSize(3);

        assertThat(popularMailBox.get(0).name()).isEqualTo("Trash");
        assertThat(popularMailBox.get(0).occurences()).isEqualTo(3);

        assertThat(popularMailBox.get(1).name()).isEqualTo("outbox");
        assertThat(popularMailBox.get(1).occurences()).isEqualTo(2);

        assertThat(popularMailBox.get(2).name()).isEqualTo("INBOX");
        assertThat(popularMailBox.get(2).occurences()).isEqualTo(2);
    }
}
