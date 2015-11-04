package de.fau.fablab.app.server.resources;

import de.fau.fablab.app.rest.api.UserApi;
import de.fau.fablab.app.rest.core.User;
import io.dropwizard.auth.Auth;

/**
 * Resource to handle User authentication and query information about users (i.e. Roles)
 */
public class UserResource implements UserApi {

    @Override
    public User getUserInfo(@Auth User user) {
        return user;
    }

}
