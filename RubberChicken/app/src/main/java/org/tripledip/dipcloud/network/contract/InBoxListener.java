package org.tripledip.dipcloud.network.contract;

/**
 * Created by Ben on 3/1/15.
 */
public interface InBoxListener<T> {

    public void onInboxItemArrived(T item);

}
