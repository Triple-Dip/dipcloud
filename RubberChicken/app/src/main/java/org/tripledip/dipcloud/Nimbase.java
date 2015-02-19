package org.tripledip.dipcloud;

import android.net.wifi.p2p.WifiP2pManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ben on 2/18/2015.
 */
public class Nimbase {

    private Map<String, Atom> atomsById;

    private Map<String, Set<AtomListener>> idListeners;

    private Map<String, Set<MoleculeListener>> channelListeners;

    public Nimbase() {
        atomsById = new HashMap<>();
        idListeners = new HashMap<>();
        channelListeners = new HashMap<>();
    }

    public void registerIdListener(String id, AtomListener atomListener) {
        if (!idListeners.containsKey(id)){
            idListeners.put(id, new HashSet<AtomListener>());
        }
        idListeners.get(id).add(atomListener);
    }

    public void registerChannelListener(String channel, MoleculeListener channelListener) {
        if (!channelListeners.containsKey(channel)){
            channelListeners.put(channel, new HashSet<MoleculeListener>());
        }
        channelListeners.get(channel).add(channelListener);
    }



}
