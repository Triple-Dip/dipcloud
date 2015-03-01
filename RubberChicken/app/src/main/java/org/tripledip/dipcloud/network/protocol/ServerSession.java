package org.tripledip.dipcloud.network.protocol;

import org.tripledip.dipcloud.local.contract.Connector;

/**
 * Created by Ben on 2/23/15.
 */
public class ServerSession {

    private final Connector<byte[]> connector;

    public ServerSession(Connector<byte[]> connector) {
        this.connector = connector;
    }

}
