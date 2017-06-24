package com.awoisoak.giphyviewer.data.local.impl;

import android.content.Context;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * SQLite implementation of GifDataStore
 */
public class SQLiteGifDataStore implements GifDataStore {
    private static final String TAG = SQLiteGifDataStore.class.getSimpleName();
    private static final Context sContext = GiphyViewerApplication.getGiphyViewerApplication();
    private final DatabaseHelper mDatabaseHelper;
    Dao<Gif, String> mGifDao;


    public SQLiteGifDataStore() {
        mDatabaseHelper = new DatabaseHelper(sContext);
        try {
            mGifDao = mDatabaseHelper.getGifDao();
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving the DAO object from the DB");
            e.printStackTrace();
        }

    }

    /**
     * Add Gif to the DB
     *
     * @param gif
     * @throws Exception
     */
    public void addGif(Gif gif) {
        try {
            mGifDao.createIfNotExists(gif);
        } catch (SQLException e) {
            Log.e(TAG, "SQLException adding a gif to the DB");
            e.printStackTrace();
        }
    }

    /**
     * Add a list of gifs to the DB
     *
     * @param gifs
     * @throws Exception
     */
    public void addGifs(final List<Gif> gifs) {
        try {
            mGifDao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Gif g : gifs) {
                        mGifDao.createIfNotExists(g);
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception adding a gif to the DB");
            e.printStackTrace();
        }
    }


    /**
     * Remove Gif from the DB
     *
     * @param id gif unique identifier
     * @return true if it was removed, false otherwise
     */
    public boolean removeGif(String id) {
        try {
            DeleteBuilder<Gif, String> gifDeleteBuilder = mGifDao.deleteBuilder();
            gifDeleteBuilder.where().eq(Gif.ID, id);
            int result = gifDeleteBuilder.delete();
            switch (result) {
                case 0:
                    Log.e(TAG, "The gif with id " + id + " couldn't be removed from the database!(Does is exist?)");
                    return false;
                case 1:
                    Log.d(TAG, "The gif with id " + id + " was removed from the database!");
                    return true;
                default:
                    throw new Exception("Unknown error: delete returned: " + result);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error removing Gif from DB");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Retrieve all Gifs
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
    public List<Gif> getAllGifs() {
        try {
            QueryBuilder<Gif, String> queryBuilder = mGifDao.queryBuilder();
            return queryBuilder.query();
        } catch (Exception e) {
            Log.e(TAG, "Exception retrieving all gifs from the DB");
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Retrieve a list of gifs given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
    public List<Gif> getGifs(int offset) {
        try {
            QueryBuilder<Gif, String> queryBuilder = mGifDao.queryBuilder();
            queryBuilder.offset((long) offset).limit((long) MAX_NUMBER_GIFS_RETURNED);
            return mGifDao.query(queryBuilder.prepare());
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving gifs in the DB from offset = " + offset);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Remove all gifs
     *
     * @throws SQLException
     */
    public void removeAllGifs() {
        try {
            mDatabaseHelper.deleteAllGifs();
        } catch (Exception e) {
            Log.e(TAG, "Error removing all gifs from the DB");
            e.printStackTrace();
        }
    }

    @Override
    public int getTotalNumberOfGifs() {
        try {
            QueryBuilder<Gif, String> queryBuilder = mGifDao.queryBuilder();
            return queryBuilder.query().size();
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving the total number of gifs in the DB");
            e.printStackTrace();
            return -1;
        }
    }
}
