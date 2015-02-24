package org.tripledip.dipcloud.contract;

import org.tripledip.dipcloud.model.Molecule;

/**
 * Created by Ben on 2/23/15.
 */
public interface Connector {

    public Molecule readNext();
    public void write(Molecule molecule);

}
