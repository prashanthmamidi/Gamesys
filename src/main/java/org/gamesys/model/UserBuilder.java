package org.gamesys.model;

import java.util.UUID;

public class UserBuilder {

    private UUID userId;
    private String userName;
    private String dob;
    private String ssn;

    public static User aUser() {
        return UserBuilder.userBuilder().build();
    }
    public static UserBuilder userBuilder() {
        return new UserBuilder()
                .withUserId(UUID.randomUUID())
                .withUserName("James")
                .withDOB("1983-11-12")
                .withSSN("123-45-6789");
    }

    public User build() {
        return new User(userId, userName, dob, ssn);
    }
    public UserBuilder withUserName(String userName) {
        this.userName = userName;
        return this;
    }
    public UserBuilder withUserId(UUID userId) {
        this.userId = userId;
        return this;
    }
    public UserBuilder withDOB(String dob) {
        this.dob = dob;
        return this;
    }
    public UserBuilder withSSN(String ssn) {
        this.ssn = ssn;
        return this;
    }
}
