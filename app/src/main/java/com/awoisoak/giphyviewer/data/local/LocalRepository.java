package com.awoisoak.giphyviewer.data.local;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.impl.GifDao;
import com.awoisoak.giphyviewer.data.local.impl.GifDatabase;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by awo on 1/17/18.
 */

public class LocalRepository {
    public static int MAX_NUMBER_GIFS_RETURNED = 1;

    @Inject
    GifDatabase mDatabase;
    @Inject
    GifDao mGifDao;
    private static final String LOG_TAG = LocalRepository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static LocalRepository sInstance;


    private LocalRepository(GifDao gifDao) {
        mGifDao = gifDao;
    }


    public synchronized static LocalRepository getInstance(GifDao gifDao) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new LocalRepository(gifDao);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Save Gif to the DB
     */

    public void addGif(Gif gif) {
        mGifDao.addGif(gif);
    }


    /**
     * Insert gif/gifs to the DB
     */
    public void addGifs(Gif... gifs) {
        mGifDao.addGifs(gifs);
    }

    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @return 1 if removed, 0 otherwise
     */
    public int removeGif(String id) {
        return mGifDao.removeGif(id);
    }


    /**
     * Retrieve all Gifs from DB
     *
     * @return Gifs List, empty list if no Gif was found
     */
    public LiveData<List<Gif>> getAllGifs() {
        return mGifDao.getAllGifs();
    }


    /**
     * Retrieve a list of gifs from DB using the Paging library
     *
     * @return Gifs List, empty list if no Gif was found
     */
    public DataSource.Factory<Integer,Gif> getGifsWithPagedList() {
        return mGifDao.getGifsWithPagedList();
    }

    /**
     * Remove all gifs from DB
     *
     * @return number of rows removed
     */
    public int removeAllGifs() {
        return mGifDao.removeAllGifs();
    }

    /**
     * Retrieve the total number of elements of DB
     */
    public int getTotalNumberOfGifs() {
        return mGifDao.getTotalNumberOfGifs();
    }


}
