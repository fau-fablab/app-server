package de.fau.fablab.app.server.core.pushservice;

import de.fau.fablab.app.rest.core.CartStatus;
import de.fau.fablab.app.rest.core.DoorState;
import de.fau.fablab.app.rest.core.PushToken;

import java.util.List;

public interface PushManger {

    void sendNotificationDoorJustOpened(List<PushToken> tokens, DoorState doorState);
    void sendCartStautsChanged(PushToken token, CartStatus status);
}
