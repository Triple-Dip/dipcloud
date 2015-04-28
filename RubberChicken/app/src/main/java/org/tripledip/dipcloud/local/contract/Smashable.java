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

    private String instanceId;

    public abstract void smashMe(SmashableBuilder smashableBuilder);

    public abstract void unsmashMe(SmashableBuilder smashableBuilder);

    public abstract Smashable newInstance();

    public String getChannel(){
        return this.getClass().getName();
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
