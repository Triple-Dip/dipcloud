package org.tripledip.dipcloud.network.util;

import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.protocol.MoleculeProtocMapper;
import org.tripledip.dipcloud.network.protocol.MoleculeProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.channels.ClosedByInterruptException;

/**
 * Created by Ben on 4/1/15.
 */
public class SocketProtocConnector implements Connector<Molecule> {

    private final Socket socket;

    public SocketProtocConnector(Socket socket) {
        this.socket = socket;
    }

    @Override
    // TODO: propagate any exception
    public Molecule readNext() throws InterruptedException {
        try {
            InputStream inputStream = socket.getInputStream();
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
            OutputStream outputStream = socket.getOutputStream();
            proto.writeDelimitedTo(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            return;
        }
    }
}
