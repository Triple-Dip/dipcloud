package org.tripledip.dipcloud.network.behavior;

import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.contract.InBoxListener;

/**
 * Created by Ben on 3/7/15.
 */
public class Session<T> {

    private final OutBox<T> outBox;
    private final InBox<T> inBox;
    private Thread inBoxThread;
    private Thread outBoxThread;

    public Session(Connector<T> connector, InBoxListener<T> inBoxListener) {
        this.outBox = new OutBox<>(connector);
        this.inBox = new InBox<>(connector, inBoxListener);
    }

    public void sendMessage(T message) {
        outBox.add(message);
    }

    public void startInBox() {
        stopInBox();
        inBoxThread = new Thread(inBox.getRunnable());
        inBoxThread.start();
    }

    public void stopInBox() {
        if (null == inBoxThread) {
            return;
        }
        inBoxThread.interrupt();
        inBoxThread = null;
    }

    public void startOutBox() {
        stopOutBox();
        outBoxThread = new Thread(outBox.getRunnable());
        outBoxThread.start();
    }

    public void stopOutBox() {
        if (null == outBoxThread) {
            return;
        }
        outBoxThread.interrupt();
        outBoxThread = null;
    }

    public boolean inBoxIsStopped() {
        return null == inBoxThread || !inBoxThread.isAlive();
    }

    public boolean outBoxIsStopped() {
        return null == outBoxThread || !outBoxThread.isAlive();
    }
}
