package org.gamesys;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.gamesys.exception.ConstraintViolationExceptionMapper;
import org.gamesys.exception.ErrorResponse;
import org.gamesys.exception.UserAlreadyExistsExceptionMapper;
import org.gamesys.exception.UserBlackListedExceptionMapper;
import org.gamesys.model.User;
import org.gamesys.model.UserDTO;
import org.gamesys.service.ExclusionService;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.ZoneOffset.UTC;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Theories.class)
public class UserResourceTest {
    private static ExclusionService exclusionService = mock(ExclusionService.class);

    @ClassRule
    public static final ResourceTestRule resources =
            ResourceTestRule.builder()
                    .addResource(new UserResource(exclusionService))
                    .addProvider(ConstraintViolationExceptionMapper.class)
                    .addProvider(UserAlreadyExistsExceptionMapper.class)
                    .addProvider(UserBlackListedExceptionMapper.class)
                    .build();

    private static final Date date = Date.from(now().minusYears(20).toInstant(UTC));

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        given(exclusionService.validate(dateToString(), "123-45-6890"))
                .willReturn(true);
    }

    @Test
    public void register_returnSuccessWithStatus201_givenValidUserDetails() throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", date, "123-45-6890");

        Response response = userRegistration(userDTO);

        assertThat(response.getStatus(), is(201));
        User user = response.readEntity(User.class);
        assertNotNull(user.getUserId());
        assertThat(user.getUserName(), is(userDTO.getUserName()));
        assertThat(user.getDob(), is(userDTO.getDob().toString()));
        assertThat(user.getSsn(), is(userDTO.getSsn()));
    }

    @DataPoints(value = "invalidUserName")
    public static List<String> invalidUserNames() {
        return Arrays.asList(null, "", " ", "invalid@123", "invalid_123");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidUserNameDetails(@FromDataPoints("invalidUserName") String userName) throws Exception {
        UserDTO userDTO = new UserDTO(userName, "Test123", date, "856-45-6789");

        Response response = userRegistration(userDTO);

        assertErrorResponse(response, "userName");
    }

    @DataPoints(value = "invalidPassword")
    public static List<String> invalidPasswords() {
        return Arrays.asList(null, "", " ", "Te1", "test1", "Teste", "TEST");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidPasswordDetails(@FromDataPoints("invalidPassword") String password) throws Exception {
        UserDTO userDTO = new UserDTO("James", password, date, "856-45-6789");

        Response response = userRegistration(userDTO);

        assertErrorResponse(response, "password");
    }

    @DataPoints(value = "invalidSSN")
    public static List<String> invalidSSN() {
        return Arrays.asList(null, "", " ", "123-45-456", "000-45-6789", "666-45-6789", "901-45-6789", "856-453-6789");
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidSSNDetails(@FromDataPoints("invalidSSN") String ssn) throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", date, ssn);

        Response response = userRegistration(userDTO);

        assertErrorResponse(response, "ssn");
    }

    @DataPoints(value = "invalidDate")
    public static List<Date> invalidDate() {
        return Arrays.asList(null, Date.from(now().plusDays(1).toInstant(UTC)));
    }

    @Theory
    @Test
    public void register_returnBadRequest_givenInValidDate(@FromDataPoints("invalidDate") Date date) throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", date, "856-45-6789");

        Response response = userRegistration(userDTO);

        assertErrorResponse(response, "dob");
    }

    @Test
    public void register_returnsBadRequestWithUserAlreadyExistsException_givenExistingUserDetails() throws Exception {
        UserDTO userDTO = new UserDTO("James", "Test1", date, "123-45-6789");
        given(exclusionService.validate(dateToString(), "123-45-6789"))
                .willReturn(true);

        Response response = userRegistration(userDTO);

        assertThat(response.getStatus(), is(400));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertThat(errorResponse.getDescription(), is("User Already Exists"));
    }

    @Test
    public void register_returnsUnAuthorisedWithUserBlackListException_givenUserIsInBlacklist() throws Exception {
        given(exclusionService.validate(dateToString(), "123-45-5890"))
                .willReturn(false);
        UserDTO userDTO = new UserDTO("James", "Test1", date, "123-45-5890");
        Response response =  userRegistration(userDTO);
        assertThat(response.getStatus(), is(401));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertThat(errorResponse.getDescription(), is("User is Black listed"));
    }

    private Response userRegistration(UserDTO userDTO) {
        return resources.client()
                .target("/user/register")
                .request()
                .header(ACCEPT, APPLICATION_JSON)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .post(Entity.entity(userDTO, APPLICATION_JSON));
    }

    private void assertErrorResponse(Response response, final String fieldName) {
        assertThat(response.getStatus(), is(400));
        ErrorResponse errorResponse = response.readEntity(ErrorResponse.class);
        assertThat(errorResponse.getDescription(), is(fieldName + " is invalid"));
    }

    private String dateToString() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(date);
    }
}

