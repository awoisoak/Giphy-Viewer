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

//TODO Create an GifDataStoreException to be the only one throw up by methods in this class?
public class SQLiteGifDataStore implements GifDataStore{
    private static final String TAG = SQLiteGifDataStore.class.getSimpleName();
    private static final Context sContext = GiphyViewerApplication.getVisorApplication();
    private final DatabaseHelper mDatabaseHelper;

    /**
     * Private constructor.
     */
    public SQLiteGifDataStore() {
        mDatabaseHelper = new DatabaseHelper(sContext);
    }

    /**
     * Add Gif to the DB
     *
     * @param gif
     * @throws Exception
     */
    public void addGif(Gif gif) throws Exception {
        try {
            Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
            gifDao.createIfNotExists(gif);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a list of gifs to the DB
     *
     * @param gifs
     * @throws Exception
     */
    public void addGifs(final List<Gif> gifs) throws Exception {
        try {
            final Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
            gifDao.callBatchTasks(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    for (Gif g : gifs) {
                        gifDao.createIfNotExists(g);
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Remove Gif from the DB
     *
     * @param id    gif unique identifier
     * @throws Exception
     */
    public void removeGif(String id) throws Exception {
            Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
            DeleteBuilder<Gif, String> gifDeleteBuilder = gifDao.deleteBuilder();
            gifDeleteBuilder.where().eq(Gif.ID, id);
            int result = gifDeleteBuilder.delete();
            switch (result) {
                case 0:
                    Log.e(TAG,"The gif with id "+id+" couldn't be removed from the database!(Does is exist?)");
                    break;
                case 1:
                    Log.d(TAG,"The gif with id "+id+" was removed from the database!");
                    break;
                default:
                    throw new Exception("Unknown error: delete returned: " + result);
            }
    }



    /**
     * Retrieve all Gifs
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
    public List<Gif> getAllGifs()
            throws Exception {
        try {
            Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
            QueryBuilder<Gif, String> queryBuilder = gifDao.queryBuilder();
            return queryBuilder.query();
            //TODO set a property SAVED_DATE with the timestamp when the gif was stored?
//            return queryBuilder.orderBy(Gif.SAVED_DATE, false).query();
        } catch (SQLException e) {
            throw new Exception("Error retrieving all the Gifs in the DB", e);
        }
    }


    /**
     * Retrieve a list of gifs given an offset
     *
     * @return Gifs List, empty list if no Gif was found
     * @throws UnknownError
     */
    public List<Gif> getGifs(int offset)
            throws Exception {
        try {
            Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
            QueryBuilder<Gif, String> queryBuilder = gifDao.queryBuilder();
            queryBuilder.offset((long) offset).limit((long) MAX_NUMBER_GIFS_RETURNED);

            //TODO set a property SAVED_DATE with the timestamp when the gif was stored?
//            queryBuilder.orderBy(Gif.SAVED_DATE,false).offset((long) offset).limit(
//                    (long) MAX_NUMBER_GIFS_RETURNED);
            return gifDao.query(queryBuilder.prepare());
        } catch (SQLException e) {
            throw new Exception("Error retrieving gifs in the DB from offset = " + offset, e);
        }
    }

    /**
     * Remove all gifs
     *
     * @throws SQLException
     */
    public void removeAllGifs() throws Exception {
        mDatabaseHelper.deleteAllGifs();
    }


    //TODO set a property SAVED_DATE with the timestamp when the gif was stored?

//    /**
//     * Retrieve the last gif entry saved in the DB
//     *
//     * @return Gifs List, empty list if no Gif was found
//     * @throws UnknownError
//     */
//    public Gif getLastGif() throws Exception {
//        Dao<Gif, String> gifDao = mDatabaseHelper.getGifDao();
//        QueryBuilder<Gif, String> queryBuilder = gifDao.queryBuilder();
//        return queryBuilder.orderBy(Gif.SAVED_DATE, false).query().get(0);
//    }


}
