package org.tripledip.dipcloud;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Wolfe on 2/18/2015.
 */
public class Molecule implements Iterable<Atom>{

    public static final int ACTION_ADD = 1;
    public static final int ACTION_UPDATE = 2;
    public static final int ACTION_REMOVE = 3;
    public static final int ACTION_SEND = 4;

    private final Set<Atom> atoms;
    private String channel;
    private int action;

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

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
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

    public void fillCollection(Collection<Atom> collection){
        collection.addAll(atoms);
    }

    @Override
    public Iterator<Atom> iterator() {
        return atoms.iterator();
    }
}
