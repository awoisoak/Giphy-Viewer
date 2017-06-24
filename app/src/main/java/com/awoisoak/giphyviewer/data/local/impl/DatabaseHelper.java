package com.awoisoak.giphyviewer.data.local.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.awoisoak.giphyviewer.data.Gif;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Helper to manage the DB
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME    = "gifs.db";
    private static final int    DATABASE_VERSION = 1;
    private Dao<Gif, String> mGifDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Gif.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource,Gif.class,true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the Gif DAO
     * @return
     * @throws SQLException
     */
    public Dao<Gif, String> getGifDao() throws SQLException {
        if (mGifDao == null) {
            mGifDao = getDao(Gif.class);
        }
        return mGifDao;
    }


    /**
     * Clear cached DAOs and close the database
     */
    @Override
    public void close() {
        mGifDao = null;
        super.close();
    }

    /**
     * Delete all gifs in the data base.
     */
    public void deleteAllGifs() throws SQLException {
        ConnectionSource connectionSource = getConnectionSource();
        TableUtils.dropTable(connectionSource, Gif.class, true);
        TableUtils.createTableIfNotExists(connectionSource, Gif.class);
    }

}

