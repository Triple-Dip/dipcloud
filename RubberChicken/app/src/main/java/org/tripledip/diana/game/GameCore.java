package org.tripledip.diana.game;

import org.tripledip.diana.game.helpers.ShipHelper;
import org.tripledip.dipcloud.local.contract.DipAccess;
import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.contract.Smashable;
import org.tripledip.dipcloud.local.model.Atom;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 4/11/2015.
 *
 * This class is supposed to be the bread and butter brain of the game. The idea is to have all the
 * events of the game: Damage/Repair Ship, Charge Shields, Destroy Ship, etc etc housed in
 * this class.
 *
 * It acts as the intermediate between the DipAccess and the UI.
 *
 * Input from the player is taken from the UI which calls methods that propose changes to the
 * DipAccess.
 *
 * This class is the listener for changes in the NimBase/Dip - these changes come in through the
 * interfaced methods which compare the changes to the current values. These changes are then
 * set locally by calling the relevant event methods. For example if the incoming ship atoms had
 * lower hp compared to the current ship, the damageShip() method would be called with the
 * difference.
 */
public class GameCore {

    private DipAccess dipAccess;
    private ShipHelper shipHelper;

    public GameCore (){

    }

    public DipAccess getDipAccess(){
        return dipAccess;
    }

    public void registerSmashable(Smashable smashable){
        dipAccess.registerSmashable(smashable);
    }

    //TODO: should wiring up the ScrudListeners be done here? Seems better in its own method
    public void setDipAccess(DipAccess dipAccess) {
        this.dipAccess = dipAccess;
    }

    //TODO: Right now this is a hack to solve the problem of how we get the initial atoms in the dip
    public void bootStrapGame(){

    }
}
