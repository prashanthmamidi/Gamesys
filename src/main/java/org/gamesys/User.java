package org.gamesys;

import java.util.UUID;

public class User {
    private UUID userId;
    private String userName;
    private String dob;
    private String ssn;

    public User() {
    }

    public User(UUID userId, String userName, String dob, String ssn) {
        this.userId = userId;
        this.userName = userName;
        this.dob = dob;
        this.ssn = ssn;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getDob() {
        return dob;
    }

    public String getSsn() {
        return ssn;
    }
}
