package com.awoisoak.giphyviewer.domain.interactors;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

public interface DatabaseInteractor {

    /**
     * Add a Gif into the DB
     *
     * @param gif
     */
    void addGif(Gif gif);

    /**
     * Remove Gif from the DB
     *
     * @param id    gif unique identifier
     */
    void removeGif(String id);


    /**
     * Retrieve a specified number of gifs from the given offset
     *
     * @param offset
     */
    List<Gif> getGifs(int offset);

    /**
     * Retrieve the numbers of Gifs in the DB
     *
     */
    int getTotalNumberOfGifs();

}
