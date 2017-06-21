package com.awoisoak.giphyviewer.data.remote;


import com.awoisoak.giphyviewer.data.remote.responses.ErrorResponse;

/**
 * Giphy Listener
 *
 * @param <T>
 */
public interface GiphyListener<T> {
    /**
     * Called when the HTTP response is in the range [200..300).
     * @param response
     */
    void onResponse(T response);

    /**
     *  {@link ErrorResponse#code} will be {@link GiphyManager#NO_CODE} if there was an IOException
     *
     * @param error
     */
    void onError(ErrorResponse error);
}
