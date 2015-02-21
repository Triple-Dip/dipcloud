package org.tripledip.dipcloud;

/**
 * Created by Ben on 2/18/2015.
 */
public interface NimterfaceListener<T> {

    public void onAdded(T thing);
    public void onUpdated(T thing);
    public void onRemoved(T thing);
    public void onSent(T thing);

}
