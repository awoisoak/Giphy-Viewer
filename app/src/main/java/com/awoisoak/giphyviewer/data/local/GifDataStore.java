package com.awoisoak.giphyviewer.data.local;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

public interface GifDataStore {
    int MAX_NUMBER_GIFS_RETURNED = 25;


    /**
     * Save Gif to the DB
     *
     * @param gif
     */
    void addGif(Gif gif);

    /**
     * Add a list of gifs to the DB
     *
     * @param gifs
     */
    void addGifs(final List<Gif> gifs);


    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @return true if it was removed, false otherwise
     */
    boolean removeGif(String id);


    /**
     * Retrieve all Gifs
     *
     * @return Gifs List, empty list if no Gif was found
     */
    List<Gif> getAllGifs();


    /**
     * Retrieve a list of gifs given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     */
    List<Gif> getGifs(int offset);

    /**
     * Remove all gifs
     *
     */
    void removeAllGifs();

    /**
     * Retrieve the total number of elements
     *
     * @return
     */
    int getTotalNumberOfGifs();

}
