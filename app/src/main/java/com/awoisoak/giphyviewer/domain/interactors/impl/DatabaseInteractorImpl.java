package com.awoisoak.giphyviewer.domain.interactors.impl;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.Repository;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;

import java.util.List;

import javax.inject.Inject;

/**
 * Interactor in charge of communicating with the Database.
 * <p>
 * (All classes in the Domain layer must be platform independent)
 */

public class DatabaseInteractorImpl implements DatabaseInteractor {

    private Repository repository;

    @Inject
    public DatabaseInteractorImpl(Repository repository) {
        this.repository = repository;
    }


    @Override
    public void addGif(final Gif gif) {
        repository.addGif(gif);

    }

    @Override
    public int removeGif(final String id) {
        return repository.removeGif(id);
    }

    @Override
    public List<Gif> getGifs(int offset) {
        return repository.getGifs(offset);
    }

    @Override
    public List<Gif> getAllGifs() {
        return repository.getAllGifs();
    }

    @Override
    public int getTotalNumberOfGifs() {
        return repository.getTotalNumberOfGifs();
    }


}
