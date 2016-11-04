package org.gamesys;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class UserDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @NotEmpty
    private String userName;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[\\d\\W]).{4,}$")
    @NotNull
    private String password;

    @NotNull
    @Past
    private Date dob;

    @Pattern(regexp = "^(?!000|666)[0-8][0-9]{2}-(?!00)[0-9]{2}-(?!0000)[0-9]{4}$")
    @NotNull
    private String ssn;

    public UserDTO(String userName, String password, Date dob, String ssn) {
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

    public Date getDob() {
        return dob;
    }

    public String getSsn() {
        return ssn;
    }
}
