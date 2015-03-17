package org.tripledip.dipcloud.local.contract;

import org.tripledip.dipcloud.local.behavior.ScrudNotifier;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Ben on 3/15/15.
 */
public interface DipAccess {

    public void proposeAdd(Molecule molecule);

    public void proposeUpdate(Molecule molecule);

    public void proposeRemove(Molecule molecule);

    public void proposeSend(Molecule molecule);

    public Crudable<Atom> getNimbase();

    public ScrudNotifier<Atom> getIdListeners();

    public ScrudNotifier<Molecule> getChannelListeners();
}
