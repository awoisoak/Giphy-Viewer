package com.awoisoak.giphyviewer.domain.interactors;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

public interface DatabaseInteractor {

    /**
     * Add a Gif into the DB
     *
     * @param gif
     */
    void addGif(Gif gif) throws Exception;

    /**
     * Remove Gif from the DB
     *
     * @param id    gif unique identifier
     */
    void removeGif(String id) throws Exception;


    /**
     * Retrieve a number of gifs from the DB
     *
     * @param offset
     */
    List<Gif> getGifs(int offset) throws Exception;

}
