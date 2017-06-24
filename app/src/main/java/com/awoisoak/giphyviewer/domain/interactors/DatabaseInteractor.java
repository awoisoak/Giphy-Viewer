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
     * @param id gif unique identifier
     * @return true if it was removed, false otherwise
     */
    boolean removeGif(String id);


    /**
     * Retrieve a specified number of gifs from the given offset
     *
     * @param offset
     */
    List<Gif> getGifs(int offset);

    /**
     * Retrieve all gifs in the DB
     */
    List<Gif> getAllGifs();

    /**
     * Retrieve the numbers of Gifs in the DB
     */
    int getTotalNumberOfGifs();

}
