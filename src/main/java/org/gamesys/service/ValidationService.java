package org.gamesys.service;

import com.google.common.collect.ImmutableMap;
import org.gamesys.model.User;
import org.gamesys.exception.UserAlreadyExistsException;

import static org.gamesys.model.UserBuilder.aUser;
import static org.gamesys.model.UserBuilder.userBuilder;

public class ValidationService {

    public static void validateExistingUser(String ssn) {
        User user = loadRegisteredUsers().get(ssn);
        if (userExists(user)) {
            throw new UserAlreadyExistsException("User Already Exists");
        }
    }

    private static ImmutableMap<String, User> loadRegisteredUsers() {
        ImmutableMap.Builder<String, User> builder = ImmutableMap.<String, User>builder();
        builder.put("123-45-6789", aUser());
        builder.put("123-45-6790", userBuilder().withSSN("123-45-6790").build());
        builder.put("123-45-6791", userBuilder().withSSN("123-45-6791").build());
        builder.put("123-45-6792", userBuilder().withSSN("123-45-6792").build());
        builder.put("123-45-6793", userBuilder().withSSN("123-45-6793").build());
        builder.put("123-45-6794", userBuilder().withSSN("123-45-6794").build());
        return builder.build();
    }

    private static boolean userExists(User user) {
        return user != null;
    }

}
