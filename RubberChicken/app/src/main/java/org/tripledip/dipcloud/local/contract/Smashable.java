package org.tripledip.dipcloud.local.contract;

import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/19/2015.
 *
 * HULKSMASH!
 *
 * extend this abstract class in your program domain objects when you wanna use the dip but you
 * don't want atoms and molecules permeating throughout your application.
 *
 */
public abstract class Smashable {

    public String getId(){
        return this.getClass().getSimpleName();
    }
    public abstract void smashMe(Molecule molecule);
    public abstract void unsmashMe(Molecule molecule);
    public abstract Smashable newInstance();

}
