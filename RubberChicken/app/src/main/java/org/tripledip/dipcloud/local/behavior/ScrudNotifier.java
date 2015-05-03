package org.tripledip.dipcloud.local.behavior;

import org.tripledip.dipcloud.local.contract.ScrudListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ben on 2/21/15.
 */
public class ScrudNotifier<T> {

    private Map<String, Set<ScrudListener<T>>> listeners;

    public ScrudNotifier() {
        this.listeners = new HashMap<>();
    }

    public void registerListener(String subject, ScrudListener<T> listener) {
        if (null == subject || null == listener) {
            return;
        }

        if (!listeners.containsKey(subject)) {
            listeners.put(subject, new HashSet<ScrudListener<T>>());
        }
        listeners.get(subject).add(listener);
    }

    public void unregisterListener(String subject, ScrudListener<T> listener) {
        if (null == subject || null == listener || !listeners.containsKey(subject)) {
            return;
        }
        listeners.get(subject).remove(listener);
    }

    public void notifyAdded (String subject, T thing) {
        if (null == subject || null == thing || !listeners.containsKey(subject)) {
            return;
        }

        Set<ScrudListener<T>> keyListeners = listeners.get(subject);
        for (ScrudListener<T> listener : keyListeners) {
            listener.onAdded(thing);
        }
        return;
    }

    public void notifyUpdated (String subject, T thing) {
        if (null == subject || null == thing || !listeners.containsKey(subject)) {
            return;
        }

        Set<ScrudListener<T>> keyListeners = listeners.get(subject);
        for (ScrudListener<T> listener : keyListeners) {
            listener.onUpdated(thing);
        }
        return;
    }

    public void notifyRemoved (String subject, T thing) {
        if (null == subject || null == thing || !listeners.containsKey(subject)) {
            return;
        }

        Set<ScrudListener<T>> keyListeners = listeners.get(subject);
        for (ScrudListener<T> listener : keyListeners) {
            listener.onRemoved(thing);
        }
        return;
    }

    public void notifySent (String subject, T thing) {
        if (null == subject || null == thing || !listeners.containsKey(subject)) {
            return;
        }

        Set<ScrudListener<T>> keyListeners = listeners.get(subject);
        for (ScrudListener<T> listener : keyListeners) {
            listener.onSent(thing);
        }
        return;
    }
}
