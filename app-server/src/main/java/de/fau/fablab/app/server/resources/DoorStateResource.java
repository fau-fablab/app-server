package de.fau.fablab.app.server.resources;

import de.fau.fablab.app.rest.api.DoorStateApi;
import de.fau.fablab.app.rest.core.DoorState;
import de.fau.fablab.app.server.configuration.DoorStateConfiguration;
import de.fau.fablab.app.server.core.pushservice.PushFacade;
import de.fau.fablab.app.server.core.spaceapi.DoorStateDAO;
import de.fau.fablab.app.server.core.spaceapi.DoorStateRequest;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;

public class DoorStateResource implements DoorStateApi {
    private final DoorStateConfiguration doorStateConfiguration;
    private final DoorStateDAO mDAO;
    private final Logger mLogger;

    public DoorStateResource(DoorStateConfiguration mConfig, DoorStateDAO aDAO) {
        doorStateConfiguration = mConfig;
        mDAO = aDAO;
        mLogger = LoggerFactory.getLogger(DoorStateResource.class);
    }

    @Override
    @UnitOfWork
    public String updateDoorState(String hash, String data) {

        if (hash == null || data == null || hash.isEmpty() || data.isEmpty())
            throw new BadRequestException("no credentials provided");

        if (doorStateConfiguration.getKeyFile() == null || doorStateConfiguration.getKeyFile().isEmpty() ||
                doorStateConfiguration.getHashAlgorithm() == null || doorStateConfiguration.getHashAlgorithm().isEmpty())
            throw new ServiceUnavailableException("key file or hash algorithm is missing in configuration");

        DoorStateRequest request = DoorStateRequest.fromData(doorStateConfiguration, hash, data);
        DoorState oldState = mDAO.getLastState();
        DoorState newState = request.getDoorState();

        // if there is no oldState use current state
        if (oldState == null)
            mDAO.saveState(newState);

        if (request.checkIfChanged(oldState)) {
            mLogger.info("DoorState changed, firing push event. Current state is " + newState);
            mDAO.saveState(newState);

            if(newState.state.equals(DoorState.State.open))
                PushFacade.getInstance().fablabDoorJustOpened(newState);
        }

        return "{\"success\":\"true\", \"state\":\"" + newState.state + "\"}";
    }
}
