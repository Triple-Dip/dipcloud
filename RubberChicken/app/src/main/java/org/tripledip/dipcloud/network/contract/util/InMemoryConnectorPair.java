package org.tripledip.dipcloud.network.contract.util;

import org.tripledip.dipcloud.network.contract.Connector;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Ben on 3/7/15.
 */
public class InMemoryConnectorPair<T> {

    private final InMemoryConnector<T> aSendToBConnector;
    private final InMemoryConnector<T> bSendToAConnector;

    public InMemoryConnectorPair() {
        MessageQueue<T> aSendToBQueue = new MessageQueue<>();
        MessageQueue<T> bSendToAQueue = new MessageQueue<>();

        aSendToBConnector = new InMemoryConnector<>(bSendToAQueue, aSendToBQueue);
        bSendToAConnector = new InMemoryConnector<>(aSendToBQueue, bSendToAQueue);
    }

    public Connector<T> getASendToB() {
        return aSendToBConnector;
    }

    public Connector<T> getBSendToA() {
        return bSendToAConnector;
    }

    private static class InMemoryConnector<T> implements Connector<T> {

        private final MessageQueue<T> readQueue;
        private final MessageQueue<T> writeQueue;

        public InMemoryConnector(MessageQueue<T> readQueue, MessageQueue<T> writeQueue) {
            this.readQueue = readQueue;
            this.writeQueue = writeQueue;
        }

        @Override
        public T readNext() throws InterruptedException {
            return readQueue.readNext();
        }

        @Override
        public void write(T outData) {
            writeQueue.write(outData);
        }
    }

    private static class MessageQueue<T> {
        private final Queue<T> written;
        private final Lock lock;
        private final Condition notEmpty;

        public MessageQueue() {
            this.written = new ConcurrentLinkedQueue<>();
            this.lock = new ReentrantLock(false);
            this.notEmpty = lock.newCondition();
        }

        public T readNext() throws InterruptedException {
            T message = written.poll();
            if (null != message) {
                return message;
            }

            try {
                lock.lockInterruptibly();
                while (null == message) {
                    notEmpty.await();
                    message = written.poll();
                }
            } finally {
                lock.unlock();
            }
            return message;
        }

        public void write(T outData) {
            written.add(outData);
            lock.lock();
            notEmpty.signal();
            lock.unlock();
        }
    }
}
