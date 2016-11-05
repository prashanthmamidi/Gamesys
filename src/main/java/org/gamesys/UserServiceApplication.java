package org.gamesys;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Environment;
import org.gamesys.exception.ConstraintViolationExceptionMapper;
import org.gamesys.exception.UserAlreadyExistsExceptionMapper;
import org.gamesys.exception.UserBlackListedExceptionMapper;
import org.gamesys.service.DummyExclusionServiceImpl;

public class UserServiceApplication extends Application<Configuration> {
    public static void main(String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }

    public void run(Configuration configuration, Environment environment) throws Exception {
        environment.jersey().register(new UserResource(new DummyExclusionServiceImpl()));

        ((DefaultServerFactory)configuration.getServerFactory()).setRegisterDefaultExceptionMappers(false);
        // Register custom mapper
        environment.jersey().register(new ConstraintViolationExceptionMapper());
        environment.jersey().register(new UserAlreadyExistsExceptionMapper());
        environment.jersey().register(new UserBlackListedExceptionMapper());
        // Restore Dropwizard's exception mappers
        environment.jersey().register(new LoggingExceptionMapper<Throwable>() {});
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new EarlyEofExceptionMapper());
    }
}
