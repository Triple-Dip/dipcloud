package org.tripledip.dipcloud.network.protocol;

import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Ben on 3/29/15.
 */
public class MoleculeProtocMapper {

    private static Molecule.Action[] MOLECULE_ACTIONS = Molecule.Action.values();

    // Convert a real Atom to a dumb data holder.
    public static MoleculeProtos.Atom toProto(Atom atom) {
        MoleculeProtos.Atom.Builder builder = MoleculeProtos.Atom.newBuilder();

        builder.setId(atom.getId());
        builder.setSequenceNumber(atom.getSequenceNumber());

        String stringData = atom.getStringData();
        if (null != stringData) {
            builder.setStringData(stringData);
        }
        builder.setIntData(atom.getIntData());
        builder.setDoubleData(atom.getDoubleData());

        return builder.build();
    }

    // Convert a dumb data holder to a real Atom.
    public static Atom fromProto(MoleculeProtos.Atom proto) {
        return new Atom(
                proto.getId(),
                proto.getSequenceNumber(),
                proto.getStringData(),
                proto.getIntData(),
                proto.getDoubleData());
    }

    // Convert a real Molecule to a dumb data holder.
    public static MoleculeProtos.Molecule toProto(Molecule molecule) {
        MoleculeProtos.Molecule.Builder builder = MoleculeProtos.Molecule.newBuilder();

        builder.setChannel(molecule.getChannel());
        builder.setAction(MoleculeProtos.Molecule.MoleculeAction.valueOf(molecule.getAction().getCode()));

        final String instanceId = molecule.getInstanceId();
        if (null != instanceId) {
            builder.setInstanceId(instanceId);
        }

        for (Atom atom : molecule) {
            builder.addAtoms(toProto(atom));
        }

        return builder.build();
    }

    // Convert a dumb data holder to a real Molecule.
    public static Molecule fromProto(MoleculeProtos.Molecule proto) {
        Collection<Atom> atoms = new ArrayList<>();
        for (MoleculeProtos.Atom atomProto : proto.getAtomsList()) {
            atoms.add(fromProto(atomProto));
        }

        Molecule.Action action = MOLECULE_ACTIONS[proto.getAction().getNumber() - 1];

        Molecule molecule = new Molecule(proto.getChannel(), action, atoms);

        if (proto.hasInstanceId()) {
            molecule.setInstanceId(proto.getInstanceId());
        }

        return molecule;
    }

}
