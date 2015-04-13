package org.tripledip.landemo;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Ben on 4/8/15.
 */
public class SocketAcceptorTask extends AsyncTask<Integer, Socket, Void> {

    private ServerSocket acceptor;

    private Listener listener;

    public Listener getListener() {
        return listener;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void cancelAcceptor() {
        if (null != acceptor) {
            try {
                acceptor.close();
            } catch (IOException e) {
            }
        }
        acceptor = null;
        cancel(true);
    }

    @Override
    protected Void doInBackground(Integer... params) {
        if (null == params || 0 == params.length) {
            return null;
        }

        try {
            acceptor = new ServerSocket(params[0]);
        } catch (IOException e) {
            return null;
        }

        while (!isCancelled()) {
            try {
                Socket socket = acceptor.accept();
                publishProgress(socket);
            } catch (IOException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Socket... values) {
        if (null == values || 0 == values.length) {
            return;
        }

        if (null == listener) {
            return;
        }

        listener.onSocketAccepted(values[0]);
    }

    public interface Listener {
        public void onSocketAccepted(Socket socket);
    }
}
