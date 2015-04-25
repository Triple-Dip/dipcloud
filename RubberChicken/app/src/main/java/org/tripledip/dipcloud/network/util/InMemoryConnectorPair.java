package org.tripledip.dipcloud.network.util;

import org.tripledip.dipcloud.network.contract.Connector;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Ben on 3/7/15.
 */
public class InMemoryConnectorPair<T> {

    protected Connector<T> aSendToBConnector;
    protected Connector<T> bSendToAConnector;
    private Random rand = new Random();
    private int jankMillis = 0;

    public InMemoryConnectorPair() {
        MessageQueue<T> aSendToBQueue = new MessageQueue<>();
        MessageQueue<T> bSendToAQueue = new MessageQueue<>();

        aSendToBConnector = new InMemoryConnector<>(bSendToAQueue, aSendToBQueue);
        bSendToAConnector = new InMemoryConnector<>(aSendToBQueue, bSendToAQueue);
    }

    public int getJankMillis() {
        return jankMillis;
    }

    public void setJankMillis(int jankMillis) {
        this.jankMillis = jankMillis;
    }

    public Connector<T> getASendToB() {
        return aSendToBConnector;
    }

    public Connector<T> getBSendToA() {
        return bSendToAConnector;
    }

    protected void awaitRandomJank() throws InterruptedException {
        if (jankMillis <= 0) {
            return;
        }

        int randomJank = rand.nextInt(jankMillis);
        Thread.sleep(randomJank);
    }

    private class InMemoryConnector<T> implements Connector<T> {

        private final MessageQueue<T> readQueue;
        private final MessageQueue<T> writeQueue;

        public InMemoryConnector(MessageQueue<T> readQueue, MessageQueue<T> writeQueue) {
            this.readQueue = readQueue;
            this.writeQueue = writeQueue;
        }

        @Override
        public T readNext() throws InterruptedException, IOException {
            T message = readQueue.readNext();
            return message;
        }

        @Override
        public void write(T outData) throws InterruptedException, IOException {
            writeQueue.write(outData);
        }
    }

    protected class MessageQueue<T> {
        private final BlockingQueue<T> written;

        public MessageQueue() {
            this.written = new LinkedBlockingDeque<>();
        }

        public T readNext() throws InterruptedException {
            T message = written.take();
            awaitRandomJank();
            return message;
        }

        public void write(T outData) {
            written.add(outData);
        }
    }
}
