package org.tripledip.dipcloud.local.contract;

import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/19/2015.
 *
 * HULKSMASH!
 *
 * Implement this interface into your program domain objects when you wanna use the dip but you
 * don't wan't atoms and molecules permeating throughout your application.
 *
 */
public interface Smashable {

    public String getId();
    public Molecule smashMe(Molecule molecule);
    public void unSmashMe(Molecule molecule);
    public Smashable newInstance();

}
