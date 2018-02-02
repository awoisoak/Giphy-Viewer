package com.awoisoak.giphyviewer.domain.interactors.impl;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.GiphyListener;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;

import javax.inject.Inject;


/**
 * Interactor in charge of communicating with the Giphy server.
 * It must be called in a background thread.
 * <p>
 * (All classes in the Domain layer must be platform independent)
 */
public class GifsRequestInteractorImpl implements GifsRequestInteractor {

    private GiphyApi api;

    @Inject
    public GifsRequestInteractorImpl(GiphyApi api) {
        this.api = api;
    }

    @Override
    public void searchGifs(String query, int offset) {
        api.search(query, offset, new GiphyListener<ListGifsResponse>() {
            @Override
            public void onResponse(ListGifsResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });

    }

    @Override
    public void getTrendingGifs() {
        api.trending(new GiphyListener<ListGifsResponse>() {
            @Override
            public void onResponse(ListGifsResponse response) {
                SignalManagerFactory.getSignalManager().postEvent(response);
            }

            @Override
            public void onError(ErrorResponse error) {
                SignalManagerFactory.getSignalManager().postEvent(error);
            }
        });
    }
}