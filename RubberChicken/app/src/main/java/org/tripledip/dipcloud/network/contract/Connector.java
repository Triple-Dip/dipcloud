package org.tripledip.dipcloud.network.contract;

/**
 * Created by Ben on 2/23/15.
 */
public interface Connector<T> {

    // TODO: throw any exception
    public T readNext() throws InterruptedException;

    // TODO: throw any exception
    public void write(T outData);

}
