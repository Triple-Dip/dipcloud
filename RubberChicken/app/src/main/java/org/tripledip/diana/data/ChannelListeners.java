package org.tripledip.diana.data;

import org.tripledip.dipcloud.local.contract.ScrudListener;
import org.tripledip.dipcloud.local.model.Molecule;

/**
 * Created by Wolfe on 3/17/2015.
 */
public class ChannelListeners {

    public class ShipListener implements ScrudListener<Molecule>{

        @Override
        public void onAdded(Molecule thing) {

        }

        @Override
        public void onUpdated(Molecule thing) {

        }

        @Override
        public void onRemoved(Molecule thing) {

        }

        @Override
        public void onSent(Molecule thing) {

        }
    }

}
