package org.tripledip.dipcloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Wolfe on 2/18/2015.
 */
public class Molecule implements Iterable<Atom>{

    public static final String DEFAULT_CHANNEL = "default";
    public static final int ACTION_GET = 1;
    public static final int ACTION_ADD = 2;
    public static final int ACTION_UPDATE = 3;
    public static final int ACTION_REMOVE = 4;

    private final Collection<Atom> atoms;
    private String channel;
    private int action;

    public Molecule(String channel, int action) {
        this.channel = channel;
        this.action = action;
        this.atoms = new ArrayList<Atom>();
    }

    public Molecule(String channel, int action, Atom... atoms) {
        this(channel, action);
        this.atoms.addAll(Arrays.asList(atoms));
    }

    public Molecule(String channel, int action, Collection<Atom> atoms) {
        this(channel, action);
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
