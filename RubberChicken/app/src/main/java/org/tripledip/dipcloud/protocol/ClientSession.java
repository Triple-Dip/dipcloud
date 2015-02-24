package org.tripledip.dipcloud.protocol;

import org.tripledip.dipcloud.contract.Connector;

/**
 * Created by Ben on 2/23/15.
 */
public class ClientSession {

    private final Connector connector;

    public ClientSession(Connector connector) {
        this.connector = connector;
    }

}
