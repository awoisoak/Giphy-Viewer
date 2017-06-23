package com.awoisoak.giphyviewer.domain.interactors.impl;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;

import java.util.List;

import javax.inject.Inject;

/**
 * Interactor in charge of communicating with the Database.
 * //TODO It might be a good idea to call it in a running thread?
 *
 * (All classes in the Domain layer must be platform independent)
 */

public class DatabaseInteractorImpl implements DatabaseInteractor {

    private GifDataStore ds;

    @Inject
    public DatabaseInteractorImpl(GifDataStore ds) {
        this.ds = ds;
    }


    @Override
    public void addGif(Gif gif) throws Exception {
        ds.addGif(gif);
    }

    @Override
    public void removeGif(String id) throws Exception {
        ds.removeGif(id);
    }

    @Override
    public List<Gif> getGifs(int offset) throws Exception {
        return ds.getGifs(offset);
    }
}
