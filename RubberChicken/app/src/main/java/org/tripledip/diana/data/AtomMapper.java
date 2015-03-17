package org.tripledip.diana.data;


import org.tripledip.diana.game.Ship;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

import java.util.Calendar;

/**
 * Created by Wolfe on 3/17/2015.
 */
public class AtomMapper {

    public static Molecule generateShipMolecule(Ship ship){
        Molecule molecule = new Molecule(Ship.SHIP);

        long timeStamp = generateTimeStamp();

        // int fields
        molecule.addOrReplace(new Atom(Ship.SHIP_HP,timeStamp,null,ship.getHp(),0));
        molecule.addOrReplace(new Atom(Ship.SHIP_AMMO,timeStamp,null,ship.getAmmo(),0));
        molecule.addOrReplace(new Atom(Ship.SHIP_ENERGY,timeStamp,null,ship.getEnergy(),0));

        // double fields
        molecule.addOrReplace(new Atom(Ship.SHIP_SHIELD,timeStamp,null,0,ship.getEnergy()));

        // string fields
        molecule.addOrReplace(new Atom(Ship.SHIP_STATUS,timeStamp,ship.getStatus(),0,0));

        return molecule;
    }

    public static long generateTimeStamp(){
        return Calendar.getInstance().getTimeInMillis();
    }

}
