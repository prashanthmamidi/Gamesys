package org.gamesys;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @NotEmpty
    private String userName;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[\\d\\W]).{4,}$")
    private String password;

    private String dob;

    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$")
    private String ssn;

    public UserDTO(String userName, String password, String dob, String ssn) {
        this.userName = userName;
        this.password = password;
        this.dob = dob;
        this.ssn = ssn;
    }

    public UserDTO() {
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public String getSsn() {
        return ssn;
    }
}
