package org.gamesys;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ValidationServiceTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void userExists_ThrowsUserAlreadyExistsException_givenExistingUser() throws Exception {
        String ssn = "123-45-6789";
        exception.expect(UserAlreadyExistsException.class);
        exception.expectMessage("User Already Exists");

        ValidationService validationService = new ValidationService();
        validationService.userExists(ssn);
    }
}
