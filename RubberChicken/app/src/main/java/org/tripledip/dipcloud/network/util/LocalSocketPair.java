package org.tripledip.dipcloud.network.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ben on 4/1/15.
 */
public class LocalSocketPair {

    private static final int ACCEPT_MILLIS = 100;

    private Socket clientSide;
    private Socket serverSide;

    public Socket getClientSide() {
        return clientSide;
    }

    public Socket getServerSide() {
        return serverSide;
    }

    public boolean open(int port) {

        ServerSocket acceptor;
        try {
            acceptor = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }

        Thread acceptorThread = startAcceptorThread(acceptor);
        clientSide = makeLocalConnection(port);

        try {
            acceptorThread.join(ACCEPT_MILLIS);
        } catch (InterruptedException e) {
        }

        try {
            acceptor.close();
        } catch (IOException e) {
            close();
            return false;
        }

        if (null == clientSide || null == serverSide
                || clientSide.isClosed() || serverSide.isClosed()) {
            close();
            return false;
        }

        return true;
    }

    public void close() {
        closeSocket(clientSide);
        clientSide = null;
        closeSocket(serverSide);
        serverSide = null;
    }

    private static void closeSocket(Socket socket) {
        if (null == socket) {
            return;
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    private Socket makeLocalConnection(int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port));
            return socket;
        } catch (IOException e) {
            return null;
        }
    }

    private Thread startAcceptorThread(final ServerSocket acceptor) {
        final Runnable waitForConnect = new Runnable() {
            @Override
            public void run() {
                try {
                    serverSide = acceptor.accept();
                } catch (IOException e) {
                    return;
                }
            }
        };

        final Thread acceptorThread = new Thread(waitForConnect);
        acceptorThread.start();
        return acceptorThread;
    }
}
