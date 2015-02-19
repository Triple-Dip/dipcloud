package org.tripledip.dipcloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by Wolfe on 2/18/2015.
 */
public class Molecule implements Iterable<Atom>{

    private final Collection<Atom> atoms;

    public Molecule() {
        this.atoms = new ArrayList<Atom>();
    }

    public Molecule(Atom... atoms) {
        this.atoms = Arrays.asList(atoms);
    }

    public Molecule(Collection<Atom> atoms) {
        this.atoms = new ArrayList<Atom>();
        this.atoms.addAll(atoms);
    }

    public int size(){
        return atoms.size();
    }

    public boolean add(Atom atom){
        return atoms.add(atom);
    }

    public Molecule findByChannel(String channel){
        Molecule subset = new Molecule();

        for(Atom atom : atoms){

            if(atom.getChannel().equals(channel)){
                subset.add(atom);
            }

        }

        return subset;
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
