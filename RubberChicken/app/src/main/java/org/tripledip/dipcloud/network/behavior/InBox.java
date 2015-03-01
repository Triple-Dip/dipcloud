package org.tripledip.dipcloud.network.behavior;

import org.tripledip.dipcloud.local.contract.Connector;
import org.tripledip.dipcloud.network.contract.InBoxListener;

/**
 * Created by Ben on 3/1/15.
 */
public class InBox<T> {

    private final Connector<T> connector;
    private final InBoxListener<T> listener;

    public InBox(Connector<T> connector, InBoxListener<T> listener) {
        this.connector = connector;
        this.listener = listener;
    }

    public void process(T item) {
        if (null == item) {
            return;
        }
        listener.onInboxItemArrived(item);
    }

    public void processNext() throws InterruptedException {
        T item = connector.readNext();
        process(item);
    }

    public Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                receiveForever();
            }
        };
    }

    private void receiveForever() {
        while (true) {
            try {
                processNext();
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
