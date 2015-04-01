package org.tripledip.dipcloud.network.util;

import com.google.protobuf.InvalidProtocolBufferException;

import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.protocol.MoleculeProtoMapper;
import org.tripledip.dipcloud.network.protocol.MoleculeProtos;

/**
 * Created by Ben on 3/29/15.
 */
public class InMemoryProtocConnectorPair extends InMemoryConnectorPair<Molecule> {

    public InMemoryProtocConnectorPair() {
        MessageQueue<byte[]> aSendToBQueue = new MessageQueue<>();
        MessageQueue<byte[]> bSendToAQueue = new MessageQueue<>();

        aSendToBConnector = new InMemoryProtocConnector(bSendToAQueue, aSendToBQueue);
        bSendToAConnector = new InMemoryProtocConnector(aSendToBQueue, bSendToAQueue);
    }


    private class InMemoryProtocConnector implements Connector<Molecule> {

        private final MessageQueue<byte[]> readQueue;
        private final MessageQueue<byte[]> writeQueue;

        public InMemoryProtocConnector(MessageQueue<byte[]> readQueue, MessageQueue<byte[]> writeQueue) {
            this.readQueue = readQueue;
            this.writeQueue = writeQueue;
        }

        @Override
        public Molecule readNext() throws InterruptedException {
            byte[] bytes = readQueue.readNext();

            MoleculeProtos.Molecule proto;
            try {
                proto = MoleculeProtos.Molecule.parseFrom(bytes);
            } catch (InvalidProtocolBufferException e) {
                return null;
            }

            return MoleculeProtoMapper.fromProto(proto);
        }

        @Override
        public void write(Molecule outData) {
            writeQueue.write(MoleculeProtoMapper.toProto(outData).toByteArray());
        }
    }
}