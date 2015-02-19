package org.tripledip.dipcloud;

/**
 * Created by Ben on 2/18/2015.
 */
public interface MoleculeListener {

    public void onAdded(Molecule molecule);
    public void onUpdated(Molecule molecule);
    public void onRemoved(Molecule molecule);
    public void onSent(Molecule molecule);

}
