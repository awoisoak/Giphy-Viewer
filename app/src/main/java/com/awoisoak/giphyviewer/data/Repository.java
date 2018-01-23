package com.awoisoak.giphyviewer.data;

import android.util.Log;

import com.awoisoak.giphyviewer.data.local.impl.GifDao;
import com.awoisoak.giphyviewer.data.local.impl.GifDatabase;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;

import java.util.List;

/**
 * Created by awo on 1/17/18.
 */

public class Repository {
    GifDatabase mDatabase ;
    GifDao mGifDao;
    private static final String LOG_TAG = Repository.class.getSimpleName();

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static Repository sInstance;


    private Repository(GifDao gifDao) {
        //TODO These dependencies shouldn't be initializated here
        GifDatabase mDatabase = GifDatabase.getInstance(GiphyViewerApplication.getGiphyViewerApplication());
        mGifDao = mDatabase.gifDao() ;
    }



    public synchronized static Repository getInstance(GifDao gifDao) {
        Log.d(LOG_TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(gifDao);
                Log.d(LOG_TAG, "Made new repository");
            }
        }
        return sInstance;
    }
    /**
     * Save Gif to the DB
     */

    public void addGif(Gif gif){
        mGifDao.addGif(gif);
    }


    /**
     * Insert gif/gifs to the DB
     */
    public void addGifs(Gif... gifs){
        mGifDao.addGifs(gifs);
    }

    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @return 1 if removed, 0 otherwise
     */
    public int removeGif(String id){
    return mGifDao.removeGif(id);
    }


    /**
     * Retrieve all Gifs from DB
     *
     * @return Gifs List, empty list if no Gif was found
     */
    public List<Gif> getAllGifs(){
        return mGifDao.getAllGifs();
    }


    /**
     * Retrieve a list of gifs from DB given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     */
    public List<Gif> getGifs(int offset){
        return mGifDao.getGifs(offset);
    }

    /**
     * Remove all gifs from DB
     * @return number of rows removed
     */
    public int removeAllGifs(){
        return mGifDao.removeAllGifs();
    }

    /**
     * Retrieve the total number of elements of DB
     */
    public int getTotalNumberOfGifs(){
        return mGifDao.getTotalNumberOfGifs();
    }



}
