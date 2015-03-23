package org.tripledip.diana.data;

import org.tripledip.diana.game.GameObject;
import org.tripledip.dipcloud.local.behavior.Nimbase;
import org.tripledip.dipcloud.local.behavior.ScrudNotifier;
import org.tripledip.dipcloud.local.behavior.SuperDip;
import org.tripledip.dipcloud.local.contract.Crudable;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;
import org.tripledip.dipcloud.network.behavior.DipClient;

/**
 * Created by Wolfe on 3/17/2015.
 */

public class Database {

    DipAccess dipAccess = new SuperDip(new Nimbase());

    public void add(GameObject gameObject)
    {
        dipAccess.proposeAdd(gameObject.atomSmash());
    }



}
