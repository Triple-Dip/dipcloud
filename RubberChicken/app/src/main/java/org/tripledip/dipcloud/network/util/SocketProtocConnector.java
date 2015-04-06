package org.tripledip.dipcloud.network.util;

import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.protocol.MoleculeProtocMapper;
import org.tripledip.dipcloud.network.protocol.MoleculeProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;

/**
 * Created by Ben on 4/1/15.
 * TODO: reading from the input stream seems to break the connector.
 * TODO: how to fix this and get real duplex?
 *
 * TODO: Reimplement with regular IO socket?
 */
public class SocketProtocConnector implements Connector<Molecule> {

    private final SocketChannel socketChannel;

    public SocketProtocConnector(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    // TODO: propagate any exception
    public Molecule readNext() throws InterruptedException {
        try {
            InputStream inputStream = socketChannel.socket().getInputStream();
            MoleculeProtos.Molecule proto = MoleculeProtos.Molecule.parseDelimitedFrom(inputStream);
            return MoleculeProtocMapper.fromProto(proto);
        } catch (ClosedByInterruptException e) {
            throw (InterruptedException) new InterruptedException("Read interrupted.").initCause(e);
        } catch (IOException e) {
            throw (InterruptedException) new InterruptedException("Read error.").initCause(e);
        }
    }

    @Override
    // TODO: propagate any exception
    public void write(Molecule outData) {
        try {
            MoleculeProtos.Molecule proto = MoleculeProtocMapper.toProto(outData);
            OutputStream outputStream = socketChannel.socket().getOutputStream();
            proto.writeDelimitedTo(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            return;
        }
    }
}
