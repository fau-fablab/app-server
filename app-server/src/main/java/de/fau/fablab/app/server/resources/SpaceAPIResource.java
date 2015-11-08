package de.fau.fablab.app.server.resources;


import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import de.fau.fablab.app.rest.api.SpaceApi;
import de.fau.fablab.app.rest.core.DoorState;
import de.fau.fablab.app.server.configuration.SpaceApiConfiguration;
import de.fau.fablab.app.server.core.pushservice.PushFacade;
import de.fau.fablab.app.server.core.spaceapi.DoorStateDAO;
import de.fau.fablab.app.server.core.spaceapi.DoorStateRequest;
import de.fau.fablab.app.server.core.spaceapi.remote.SpaceAPIService;
import io.dropwizard.hibernate.UnitOfWork;
import net.spaceapi.HackerSpace;
import org.glassfish.jersey.client.proxy.WebResourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAllowedException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/***
 * Resource class for our /spaceapi uri
 * NOTE: This class is being used by multiple threads concurrently
 */
public class SpaceAPIResource implements SpaceApi
{
    private final SpaceApiConfiguration mConfig;

    public SpaceAPIResource(SpaceApiConfiguration aConfig) {
        mConfig = aConfig;
    }

    @Override
    public HackerSpace getSpace(String name) {

        if (!name.equalsIgnoreCase(mConfig.getSpace()))
            throw new NotAllowedException("This is not allowed");

        WebTarget target = ClientBuilder.newClient().register(JacksonJsonProvider.class).target(mConfig.getEndpoint());
        SpaceAPIService api = WebResourceFactory.newResource(SpaceAPIService.class, target);

        return api.space();
    }
}
