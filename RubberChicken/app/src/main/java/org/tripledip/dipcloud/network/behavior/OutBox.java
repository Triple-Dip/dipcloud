package org.tripledip.dipcloud.network.behavior;

import android.util.Log;

import org.tripledip.dipcloud.network.contract.Connector;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Ben on 3/1/15.
 */
public class OutBox<T> {

    private final Connector<T> connector;
    private final BlockingQueue<T> toSend;

    public OutBox(Connector<T> connector) {
        this.connector = connector;
        this.toSend = new LinkedBlockingQueue<>();
    }

    public void add(T item) {
        Log.i(OutBox.class.getName(), "add toSend: " + item);
        toSend.add(item);
    }

    public boolean sendNext() throws InterruptedException, IOException {
        T next = toSend.take();
        Log.i(OutBox.class.getName(), "send out: " + next);
        connector.write(next);
        return true;
    }

    public int sendAll() throws InterruptedException, IOException {
        int sent = 0;
        while (sendNext()) {
            sent++;
        }
        return sent;
    }

    public Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                sendForever();
            }
        };
    }

    // TODO: would like a handler to call back for IOException
    private void sendForever() {
        while (true) {
            try {
                this.sendAll();
            } catch (InterruptedException e) {
                Log.i(OutBox.class.getName(), "InterruptedException: " + e.getMessage());
                return;
            } catch (IOException e) {
                Log.e(OutBox.class.getName(), "IOException: " + e.getMessage());
                return;
            }
        }
    }
}
