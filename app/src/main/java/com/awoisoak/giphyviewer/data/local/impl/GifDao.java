package com.awoisoak.giphyviewer.data.local.impl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

/**
 * Created by awo on 1/17/18.
 */
@Dao
public interface GifDao {

    /**
     * Save Gif to the DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGif(Gif gif);


    /**
     * Insert gif/gifs to the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGifs(Gif... gifs);

    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @return 1 if removed, 0 otherwise
     */
    //TODO this can't return a boolean
    @Query("DELETE FROM gifs WHERE server_id = :id")
    int removeGif(String id);


    /**
     * Retrieve all Gifs
     *
     * @return Gifs List, empty list if no Gif was found
     */
    @Query("SELECT * FROM gifs")
    LiveData<List<Gif>> getAllGifs();


    /**
     * Retrieve a list of gifs given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     */
    @Query("SELECT * FROM gifs LIMIT :limit OFFSET :offset")
    LiveData<List<Gif>> getGifs(int limit, int offset);

    /**
     * Remove all gifs
     * @return number of rows removed
     */
    @Query("DELETE FROM gifs")
    int removeAllGifs();

    /**
     * Retrieve the total number of elements
     */
    @Query("SELECT COUNT(*) FROM gifs")
    int getTotalNumberOfGifs();
}
