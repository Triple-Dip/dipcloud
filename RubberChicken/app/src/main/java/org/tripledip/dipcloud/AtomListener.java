package org.tripledip.dipcloud;

/**
 * Created by Ben on 2/18/2015.
 */
public interface AtomListener {

    public void onAdded(Atom atom);
    public void onUpdated(Atom atom);
    public void onRemoved(Atom atom);
    public void onSent(Atom atom);

}
