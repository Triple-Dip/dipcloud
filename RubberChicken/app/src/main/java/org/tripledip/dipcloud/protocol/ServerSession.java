package org.tripledip.dipcloud.protocol;

import org.tripledip.dipcloud.contract.Connector;

/**
 * Created by Ben on 2/23/15.
 */
public class ServerSession {

    private final Connector connector;

    public ServerSession(Connector connector) {
        this.connector = connector;
    }

}
