package edu.java.bot.service;

import edu.java.bot.model.Link;
import edu.java.bot.model.User;
import edu.java.bot.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private final static Long TEST_ID = 1L;

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void findByChatIdAbsentUser() {
        when(userRepository.findByChatId(Mockito.anyLong())).thenReturn(Optional.empty());

        Optional<User> expectedUser = Optional.empty();
        Optional<User> actualUser = userService.findByChatId(TEST_ID);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void findByChatIdExistingUser() {
        User user = new User();
        user.setChatId(TEST_ID);

        when(userRepository.findByChatId(Mockito.anyLong())).thenReturn(Optional.of(user));

        Optional<User> expectedUser = Optional.of(user);
        Optional<User> actualUser = userService.findByChatId(TEST_ID);

        Assertions.assertEquals(expectedUser, actualUser);
    }

    @Test
    void absentLinkShouldReturnFalseWhenAskWasNotLinkTracked() {
        User user = Mockito.mock(User.class);
        Link link = new Link();

        when(user.getLinks()).thenReturn(List.of());

        Assertions.assertFalse(userService.wasLinkTracked(user, link));
    }

    @Test
    void absentLinkShouldReturnTrueWhenAskWasLinkTracked() {
        User user = Mockito.mock(User.class);
        Link link = new Link();

        when(user.getLinks()).thenReturn(List.of(link));

        Assertions.assertTrue(userService.wasLinkTracked(user, link));
    }

    @Test
    void savingUserShouldRepeatOneTime() {
        User user = new User();
        userService.save(user);
        Mockito.verify(userRepository, Mockito.times(1)).save(user);
    }

}
