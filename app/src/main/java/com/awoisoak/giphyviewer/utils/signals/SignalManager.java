package com.awoisoak.giphyviewer.utils.signals;

/**
 * Interface to be used by the corresponding bus event library wrapper
 */

public interface SignalManager {

    void register(Object object);

    void unregister(Object object);

    void postEvent(Object event);
}
