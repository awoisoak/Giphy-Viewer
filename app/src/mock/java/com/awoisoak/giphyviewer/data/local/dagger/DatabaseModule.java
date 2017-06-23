package com.awoisoak.giphyviewer.data.local.dagger;

import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.data.local.impl.SQLiteGifDataStore;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    GifDataStore provideDataStore()  {
        return new SQLiteGifDataStore();
        //TODO in case we would like to mock the database
//        return new MockSQLiteGifDataStore();
    }
}
