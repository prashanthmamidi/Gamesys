package org.gamesys;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.gamesys.exception.ConstraintViolationExceptionMapper;
import org.gamesys.exception.ErrorResponse;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import java.util.Arrays;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(Theories.class)
public class UserResourceTest {
    @ClassRule
    public static final ResourceTestRule resources =
            ResourceTestRule.builder()
                    .addResource(new UserResource())
                    .addProvider(ConstraintViolationExceptionMapper.class)
                    .build();

    @Test
    public void register_returnSuccessWithStatus201_givenValidUserDetails() throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", "1985-03-31", "123-45-6789");

        Response response = userRegistration(userDTO);

        assertThat(response.getStatus(), is(201));
        User user = response.readEntity(User.class);
        assertNotNull(user.getUserId());
        assertThat(user.getUserName(), is(userDTO.getUserName()));
        assertThat(user.getDob(), is(userDTO.getDob()));
        assertThat(user.getSsn(), is(userDTO.getSsn()));
    }

    @DataPoints(value = "invalidUserName")
    public static List<String> invalidUserNames() {
        return Arrays.asList("", " ", "invalid@123", "invalid_123");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidUserNameDetails(@FromDataPoints("invalidUserName") String userName) throws Exception {
        UserDTO userDTO = new UserDTO(userName, "Test123", "1985-03-31", "856-45-6789");

        Response response = userRegistration(userDTO);

        assertErrorResponse(response);
    }

    @DataPoints(value = "invalidPassword")
    public static List<String> invalidPasswords() {
        return Arrays.asList("", " ", "Te1", "test1", "Teste", "TEST");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidPasswordDetails(@FromDataPoints("invalidPassword") String password) throws Exception {
        UserDTO userDTO = new UserDTO("James", password, "1985-03-31", "856-45-6789");

        Response response = userRegistration(userDTO);

        assertErrorResponse(response);
    }

    @DataPoints(value = "invalidSSN")
    public static List<String> invalidSSN() {
        return Arrays.asList("", " ", "123-45-456", "000-45-6789", "666-45-6789", "901-45-6789", "856-453-6789");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidSSNDetails(@FromDataPoints("invalidSSN") String ssn) throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", "1985-03-31", ssn);

        Response response = userRegistration(userDTO);

        assertErrorResponse(response);
    }

    private Response userRegistration(UserDTO userDTO) {
        return resources.client()
                .target("/user/register")
                .request()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(Entity.entity(userDTO, APPLICATION_JSON));
    }

    private void assertErrorResponse(Response response) {
        assertThat(response.getStatus(), is(400));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertThat(errorResponse.getDescription(), is("Invalid Request"));
    }
}

