package org.gamesys;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.gamesys.exception.ConstraintViolationExceptionMapper;
import org.gamesys.exception.UserAlreadyExistsExceptionMapper;
import org.gamesys.exception.UserBlackListedExceptionMapper;

public class UserServiceApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }

    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserResource(new DummyExclusionServiceImpl()));
        environment.jersey().register(new ConstraintViolationExceptionMapper());
        environment.jersey().register(new UserAlreadyExistsExceptionMapper());
        environment.jersey().register(new UserBlackListedExceptionMapper());
    }
}
