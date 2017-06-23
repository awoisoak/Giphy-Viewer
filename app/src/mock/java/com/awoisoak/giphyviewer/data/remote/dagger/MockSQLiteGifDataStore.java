package com.awoisoak.giphyviewer.data.remote.dagger;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.GifDataStore;

import java.util.List;

/**
 *  Mock SQLiteGifDataStore Manager
 *
 *  Class skeleton to implement a mock database if needed
 */

public class MockSQLiteGifDataStore implements GifDataStore {
    @Override
    public void addGif(Gif gif) {

    }

    @Override
    public void addGifs(List<Gif> gifs) {

    }

    @Override
    public void removeGif(String id) {

    }

    @Override
    public List<Gif> getAllGifs() {
        return null;
    }

    @Override
    public List<Gif> getGifs(int offset) {
        return null;
    }

    @Override
    public void removeAllGifs() {

    }

    @Override
    public int getTotalNumberOfGifs() {
        return 0;
    }
}
