package org.tripledip.dipcloud.local.contract;

/**
 * Created by Ben on 2/23/15.
 */
public interface Connector<T> {

    public T readNext();
    public void write(T outData);

}
