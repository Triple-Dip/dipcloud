package org.tripledip.dipcloud.network.contract;

import java.io.IOException;

/**
 * Created by Ben on 2/23/15.
 */
public interface Connector<T> {

    public T readNext() throws InterruptedException, IOException;

    public void write(T outData) throws InterruptedException, IOException;

}
