package org.gamesys.service;

import org.gamesys.exception.UserAlreadyExistsException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.gamesys.service.ValidationService.validateExistingUser;

public class ValidationServiceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void userExists_ThrowsUserAlreadyExistsException_givenExistingUser() throws Exception {
        exception.expect(UserAlreadyExistsException.class);
        exception.expectMessage("User Already Exists");

        validateExistingUser("123-45-6789");
    }
}
