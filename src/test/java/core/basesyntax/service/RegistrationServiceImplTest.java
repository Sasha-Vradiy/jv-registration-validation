package core.basesyntax.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.exception.RegistrationException;
import core.basesyntax.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationServiceImplTest {
    private RegistrationService registrationService;
    private User validUser;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationServiceImpl();
        Storage.people.clear();
        validUser = new User();
        validUser.setLogin("alice_doe");
        validUser.setPassword("password123");
        validUser.setAge(25);
    }

    @Test
    void register_validUser_Ok() {
        User result = registrationService.register(validUser);
        assertNotNull(result);
        assertEquals(validUser.getLogin(), result.getLogin());
        assertEquals(1, Storage.people.size());
    }

    @Test
    void register_nullUser_notOk() {
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(null);
        });
    }

    @Test
    void register_shortLogin_notOk() {
        validUser.setLogin("abcde");
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(validUser);
        });
    }

    @Test
    void register_minLoginLength() {
        validUser.setLogin("abcdef");
        User result = registrationService.register(validUser);
        assertNotNull(result);
    }

    @Test
    void register_nullPassword_notOk() {
        validUser.setPassword(null);
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(validUser);
        });
    }

    @Test
    void register_shortPassw0rd_notOk() {
        validUser.setPassword("12345");
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(validUser);
        });
    }

    @Test
    void register_minPasswordLenght_Ok() {
        validUser.setPassword("123456");
        User result = registrationService.register(validUser);
        assertNotNull(result);
    }

    @Test
    void registerNullAge_notOk() {
        validUser.setAge(null);
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(validUser);
        });
    }

    @Test
    void register_underage_notOk() {
        validUser.setAge(17);
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(validUser);
        });
    }

    @Test
    void register_minAge_Ok() {
        validUser.setAge(18);
        User result = registrationService.register(validUser);
        assertNotNull(result);
    }

    @Test
    void register_duplicateLogin_notOk() {
        registrationService.register(validUser);
        User duplicateUser = new User();
        duplicateUser.setLogin(validUser.getLogin());
        duplicateUser.setPassword("differentPass");
        duplicateUser.setAge(30);
        assertThrows(RegistrationException.class, () -> {
            registrationService.register(duplicateUser);
        });
    }
}
