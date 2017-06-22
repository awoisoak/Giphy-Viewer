package com.awoisoak.giphyviewer.data.local;

import com.awoisoak.giphyviewer.data.Gif;

import java.sql.SQLException;
import java.util.List;

public interface GifDataStore {
    int MAX_NUMBER_GIFS_RETURNED = 25;


    /**
     * Save Gif to the DB
     *
     * @param gif
     * @throws Exception
     */
     void addGif(Gif gif) throws Exception;

    /**
     * Add a list of gifs to the DB
     *
     * @param gifs
     * @throws Exception
     */
     void addGifs(final List<Gif> gifs) throws Exception;


    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @throws Exception
     */
     void removeGif(String id) throws Exception;


    /**
     * Retrieve all Gifs
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
     List<Gif> getAllGifs() throws Exception;


    /**
     * Retrieve a list of gifs given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
    List<Gif> getGifs(int offset) throws Exception;

    /**
     * Remove all gifs
     *
     * @throws SQLException
     */
    void removeAllGifs() throws Exception;

}
