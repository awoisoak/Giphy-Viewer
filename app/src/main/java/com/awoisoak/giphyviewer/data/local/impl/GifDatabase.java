package com.awoisoak.giphyviewer.data.local.impl;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;

/**
 * Created by awo on 1/17/18.
 */
@Database(entities = {Gif.class},version=1)
public abstract class GifDatabase extends RoomDatabase {

    private static final String LOG_TAG = GifDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "gifs";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static GifDatabase sInstance;

    public static GifDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database instance...");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        GifDatabase.class, GifDatabase.DATABASE_NAME).build();
                Log.d(LOG_TAG, "New database created");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract GifDao gifDao();

}
