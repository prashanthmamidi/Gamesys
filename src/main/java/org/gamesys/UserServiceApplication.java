package org.gamesys;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.gamesys.exception.ConstraintViolationExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceApplication extends Application<Configuration> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceApplication.class);
    public static void main(String[] args) throws Exception {
        new UserServiceApplication().run(args);
    }

    public void run(Configuration configuration, Environment environment) throws Exception {
        LOGGER.info("Method App#run() called");
        environment.jersey().register(new UserResource());
        environment.jersey().register(new ConstraintViolationExceptionMapper());
    }
}
