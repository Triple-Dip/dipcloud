package org.tripledip.dipcloud;

/**
 * Created by Ben on 2/21/15.
 */
public interface Scrudable<T> extends Crudable<T> {

    public boolean send(T thing);

}
