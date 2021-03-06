package org.tripledip.diana.service;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * Created by Ben on 4/8/15.
 */
public class SocketConnectorTask extends AsyncTask<SocketAddress, Void, Socket> {

    public static final int DEFAULT_TIMEOUT_MILLIS = 1000;
    private int timeoutMillis = DEFAULT_TIMEOUT_MILLIS;
    private SocketListener listener;
    private Socket socket;

    public SocketListener getListener() {
        return listener;
    }

    public void setListener(SocketListener listener) {
        this.listener = listener;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public void cancelConnector() {
        if (null != socket) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket = null;
        cancel(true);
    }


    @Override
    protected Socket doInBackground(SocketAddress... params) {
        if (null == params || 0 == params.length) {
            return null;
        }

        final SocketAddress remoteAddress = params[0];

        socket = new Socket();

        try {
            socket.connect(remoteAddress, timeoutMillis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return socket;
    }

    @Override
    protected void onPostExecute(Socket socket) {
        if (null == listener) {
            return;
        }
        listener.onSocketConnected(socket);
    }
}
