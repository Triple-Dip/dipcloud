package org.tripledip.dipcloud.local.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private final Set<Atom> atoms;
    private String channel;
    private Action action;

    public Molecule(String channel) {
        this.channel = channel;
        this.atoms = new HashSet<>();
    }

    public Molecule(String channel, Atom... atoms) {
        this(channel);
        this.atoms.addAll(Arrays.asList(atoms));
    }

    public Molecule(String channel, Collection<Atom> atoms) {
        this(channel);
        this.atoms.addAll(atoms);
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Set<Atom> getAtoms() {
        return atoms;
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

    public boolean add(Atom atom){
        return atoms.add(atom);
    }

    public Atom findById(String id){
        for(Atom atom : atoms){
            if(atom.getId().equals(id)){
                return atom;
            }
        }
        return null;
    }

    public Collection<Atom> fillCollection(Collection<Atom> collection){
        collection.addAll(atoms);
        return collection;
    }

    @Override
    public Iterator<Atom> iterator() {
        return atoms.iterator();
    }
}
