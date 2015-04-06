package org.tripledip.dipcloud.network.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractInterruptibleChannel;

/**
 * Created by Ben on 4/1/15.
 *
 * TODO: reimplement with regular IO, short-lived acceptor thread
 */
public class LocalSocketPair {

    private SocketChannel clientSide;
    private SocketChannel serverSide;

    public static void closeChannel(AbstractInterruptibleChannel channel) {
        if (null == channel) {
            return;
        }

        try {
            channel.close();
        } catch (IOException e) {
        }
    }

    public SocketChannel getClientSide() {
        return clientSide;
    }

    public SocketChannel getServerSide() {
        return serverSide;
    }

    public boolean open(int port) {

        clientSide = openSocket();

        ServerSocketChannel acceptor = openAcceptor(port);
        if (null == acceptor) {
            closeChannel(clientSide);
            return false;
        }

        serverSide = acceptSocket(acceptor, clientSide);
        closeChannel(acceptor);

        if (null == serverSide) {
            closeChannel(clientSide);
            return false;
        }

        if (!clientSide.isConnected() || !serverSide.isConnected()) {
            close();
            return false;
        }

        return true;
    }

    public void close() {
        closeChannel(clientSide);
        closeChannel(serverSide);
    }

    private SocketChannel openSocket() {
        SocketChannel socket;
        try {
            socket = SocketChannel.open();
        } catch (IOException e) {
            return null;
        }

        try {
            socket.configureBlocking(false);
        } catch (IOException e) {
            closeChannel(socket);
            return null;
        }

        return socket;
    }

    private ServerSocketChannel openAcceptor(int port) {
        ServerSocketChannel acceptor;
        try {
            acceptor = ServerSocketChannel.open();
        } catch (IOException e) {
            return null;
        }

        try {
            acceptor.configureBlocking(true);
            acceptor.socket().bind(new InetSocketAddress(port));
        } catch (IOException e) {
            closeChannel(acceptor);
            return null;
        }

        return acceptor;
    }

    private SocketChannel acceptSocket(ServerSocketChannel acceptor, SocketChannel client) {
        SocketChannel server;
        try {
            final int port = acceptor.socket().getLocalPort();
            client.connect(new InetSocketAddress(InetAddress.getLocalHost(), port));
            server = acceptor.accept();
            client.finishConnect();
            client.configureBlocking(true);
        } catch (IOException e) {
            return null;
        }

        return server;
    }
}
