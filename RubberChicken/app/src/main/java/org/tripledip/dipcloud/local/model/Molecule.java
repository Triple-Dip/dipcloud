package org.tripledip.dipcloud.local.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Wolfe on 2/18/2015.
 */
public class Molecule implements Iterable<Atom>{

    public enum Action {
        ADD(1), UPDATE(2), REMOVE(3), SEND(4);
        private int code;
        Action(int code) {
            this.code = code;
        }
        public int getCode() {
            return code;
        }
    }

    private final Map<String, Atom> atoms;
    private final String channel;
    private Action action;

    public Molecule(String channel) {
        this.channel = channel;
        this.atoms = new HashMap<>();
    }

    public Molecule(String channel, Atom... atoms) {
        this(channel);
        addOrReplaceAll(Arrays.asList(atoms));
    }

    public Molecule(String channel, Collection<Atom> atoms) {
        this(channel);
        addOrReplaceAll(atoms);
    }

    public Molecule(String channel, Action action, Collection<Atom> atoms) {
        this(channel);
        this.action = action;
        addOrReplaceAll(atoms);
    }

    public String getChannel() {
        return channel;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public int size(){
        return atoms.size();
    }

    public Atom addOrReplace(Atom atom){
        return atoms.put(atom.getId(), atom);
    }

    public int addOrReplaceAll(Collection<Atom> atoms) {
        int nAdded = 0;
        for (Atom atom : atoms) {
            nAdded += null == addOrReplace(atom) ? 1 : 0;
        }
        return nAdded;
    }

    public Atom findById(String id){
        if (atoms.containsKey(id)) {
            return atoms.get(id);
        }
        return null;
    }

    public Collection<Atom> fillCollection(Collection<Atom> collection){
        collection.addAll(atoms.values());
        return collection;
    }

    @Override
    public Iterator<Atom> iterator() {
        return atoms.values().iterator();
    }
}
