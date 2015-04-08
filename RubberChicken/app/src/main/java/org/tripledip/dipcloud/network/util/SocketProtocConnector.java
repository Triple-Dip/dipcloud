package org.tripledip.dipcloud.network.util;

import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.contract.Connector;
import org.tripledip.dipcloud.network.protocol.MoleculeProtocMapper;
import org.tripledip.dipcloud.network.protocol.MoleculeProtos;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Ben on 4/1/15.
 */
public class SocketProtocConnector implements Connector<Molecule> {

    private final Socket socket;

    public SocketProtocConnector(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Molecule readNext() throws InterruptedException, IOException {
        InputStream inputStream = socket.getInputStream();

        MoleculeProtos.Molecule proto = MoleculeProtos.Molecule.parseDelimitedFrom(inputStream);
        if (null == proto) {
            return null;
        }
        return MoleculeProtocMapper.fromProto(proto);
    }

    @Override
    public void write(Molecule outData) throws InterruptedException, IOException {
        MoleculeProtos.Molecule proto = MoleculeProtocMapper.toProto(outData);
        OutputStream outputStream = socket.getOutputStream();
        proto.writeDelimitedTo(outputStream);
        outputStream.flush();
    }
}
