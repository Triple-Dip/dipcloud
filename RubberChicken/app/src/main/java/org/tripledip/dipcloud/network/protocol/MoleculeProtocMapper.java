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
        builder.setTimestamp(atom.getTimeStamp());
        builder.setStringData(atom.getStringData());
        builder.setIntData(atom.getIntData());
        builder.setDoubleData(atom.getDoubleData());

        return builder.build();
    }

    // Convert a dumb data holder to a real Atom.
    public static Atom fromProto(MoleculeProtos.Atom proto) {
        return new Atom(
                proto.getId(),
                proto.getTimestamp(),
                proto.getStringData(),
                proto.getIntData(),
                proto.getDoubleData());
    }

    // Convert a real Molecule to a dumb data holder.
    public static MoleculeProtos.Molecule toProto(Molecule molecule) {
        MoleculeProtos.Molecule.Builder builder = MoleculeProtos.Molecule.newBuilder();

        builder.setChannel(molecule.getChannel());
        builder.setAction(MoleculeProtos.Molecule.MoleculeAction.valueOf(molecule.getAction().getCode()));

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

        return new Molecule(proto.getChannel(), action, atoms);
    }

}
