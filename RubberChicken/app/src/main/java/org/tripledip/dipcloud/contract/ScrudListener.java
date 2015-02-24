package org.tripledip.dipcloud.contract;

/**
 * Created by Ben on 2/18/2015.
 */
public interface ScrudListener<T> {

    public void onAdded(T thing);
    public void onUpdated(T thing);
    public void onRemoved(T thing);
    public void onSent(T thing);

}
