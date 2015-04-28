package org.tripledip.diana.service;

import java.net.Socket;

/**
 * Created by Ben on 4/27/15.
 */
public interface SocketListener {
    public void onSocketConnected(Socket socket);
}
