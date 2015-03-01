package org.tripledip.dipcloud.local.contract;

/**
 * Created by Ben on 2/23/15.
 */
public interface Connector<T> {

    public T readNext() throws InterruptedException;

    public void write(T outData);

}
