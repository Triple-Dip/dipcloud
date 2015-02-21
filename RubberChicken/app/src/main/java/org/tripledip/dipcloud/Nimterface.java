package org.tripledip.dipcloud;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ben on 2/21/15.
 */
public class Nimterface {

    private Crudable<Atom> nimbase;
    private Map<String, Set<NimterfaceListener<Molecule>>> channelListeners;
    private Map<String, Set<NimterfaceListener<Atom>>> idListeners;

    public Nimterface(Crudable nimbase) {
        this.nimbase = nimbase;
        this.channelListeners = new HashMap<>();
        this.idListeners = new HashMap<>();
    }

    public int size() {
        return nimbase.size();
    }

    public Atom get(String id) {
        return nimbase.get(id);
    }

    public void registerChannelListener(String channel, NimterfaceListener<Molecule> listener) {
        if (null == channel || null == listener) {
            return;
        }

        if (!channelListeners.containsKey(channel)) {
            channelListeners.put(channel, new HashSet<NimterfaceListener<Molecule>>());
        }
        channelListeners.get(channel).add(listener);
    }

    public void registerIdListener(String id, NimterfaceListener<Atom> listener) {
        if (null == id|| null == listener) {
            return;
        }

        if (!idListeners.containsKey(id)) {
            idListeners.put(id, new HashSet<NimterfaceListener<Atom>>());
        }
        idListeners.get(id).add(listener);
    }

    public boolean add(Molecule molecule) {
        molecule.setAction(Molecule.ACTION_ADD);
        return processAndNotify(molecule);
    }

    public boolean update(Molecule molecule) {
        molecule.setAction(Molecule.ACTION_UPDATE);
        return processAndNotify(molecule);
    }

    public boolean remove(Molecule molecule) {
        molecule.setAction(Molecule.ACTION_REMOVE);
        return processAndNotify(molecule);
    }

    public boolean send(Molecule molecule) {
        molecule.setAction(Molecule.ACTION_SEND);
        return processAndNotify(molecule);
    }

    private boolean processAndNotify(Molecule molecule) {
        boolean anythingChanged = processAtoms(molecule);
        if (anythingChanged) {
            notifyListeners(channelListeners, molecule.getChannel(), molecule, molecule.getAction());
        }
        return anythingChanged;
    }

    // Apply the molecule action to each atom.
    private boolean processAtoms(Molecule molecule) {
        boolean anythingChanged = false;
        switch (molecule.getAction()) {

            case Molecule.ACTION_ADD:
                for (Atom atom : molecule) {
                    if (nimbase.add(atom)) {
                        notifyListeners(idListeners, atom.getId(), atom, molecule.getAction());
                        anythingChanged = true;
                    }
                }
                return anythingChanged;

            case Molecule.ACTION_UPDATE:
                for (Atom atom : molecule) {
                    if (nimbase.update(atom)) {
                        notifyListeners(idListeners, atom.getId(), atom, molecule.getAction());
                        anythingChanged = true;
                    }                }
                return anythingChanged;

            case Molecule.ACTION_REMOVE:
                for (Atom atom : molecule) {
                    if (nimbase.remove(atom)) {
                        notifyListeners(idListeners, atom.getId(), atom, molecule.getAction());
                        anythingChanged = true;
                    }                }
                return anythingChanged;

            case Molecule.ACTION_SEND:
                return true;

            default:
                return false;
        }
    }

    // Notify listeners that the nimbase has changed.
    private <T> void notifyListeners (Map<String, Set<NimterfaceListener<T>>> map, String key, T thing, int action) {
        if (null == map || null == key || null == thing || !map.containsKey(key)) {
            return;
        }

        Set<NimterfaceListener<T>> listeners = map.get(key);

        switch (action) {

            case Molecule.ACTION_ADD:
                for (NimterfaceListener<T> listener : listeners) {
                    listener.onAdded(thing);
                }
                return;

            case Molecule.ACTION_UPDATE:
                for (NimterfaceListener<T> listener : listeners) {
                    listener.onUpdated(thing);
                }
                return;

            case Molecule.ACTION_REMOVE:
                for (NimterfaceListener<T> listener : listeners) {
                    listener.onRemoved(thing);
                }
                return;

            case Molecule.ACTION_SEND:
                for (NimterfaceListener<T> listener : listeners) {
                    listener.onSent(thing);
                }
                return;

            default:
                return;
        }

    }

}
