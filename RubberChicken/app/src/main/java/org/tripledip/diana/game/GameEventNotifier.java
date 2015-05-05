package org.tripledip.diana.game;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Created by Wolfe on 4/26/2015.
 */
public class GameEventNotifier<T> {

    private Map<String, Set<GameEventListener<T>>> listeners;

    public GameEventNotifier() {
        this.listeners = new HashMap<>();
    }

    public void registerListener(String subject, GameEventListener<T> listener) {
        if (null == subject || null == listener) {
            return;
        }

        if (!listeners.containsKey(subject)) {
            listeners.put(subject, new HashSet<GameEventListener<T>>());
        }
        listeners.get(subject).add(listener);
    }

    public void unRegisterListener(String subject, GameEventListener<T> listener){
        if (null == subject || null == listener) {
            return;
        }

        if (listeners.containsKey(subject)) {
            listeners.get(subject).remove(listener);
        }

    }
    public void notifyEventOccurred (String subject, T thing) {
        if (null == subject || null == thing || !listeners.containsKey(subject)) {
            return;
        }

        Set<GameEventListener<T>> keyListeners = listeners.get(subject);
        for (GameEventListener listener : keyListeners) {
            listener.onEventOccurred(subject, thing);
        }
        return;
    }

}
