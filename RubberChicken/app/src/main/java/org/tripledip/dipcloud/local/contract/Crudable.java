package org.tripledip.dipcloud.local.contract;

import java.util.Collection;
import java.util.Comparator;

/**
 * Created by Ben on 2/21/15.
 */
public interface Crudable<T> {

    public T get(String id);
    public boolean add(T thing);
    public boolean update(T thing);
    public boolean remove(T thing);
    public int size();

    public Collection<T> fillCollection(Collection<T> collection);
    public T[] toOrderedArray(Comparator<T> comparator);
}
