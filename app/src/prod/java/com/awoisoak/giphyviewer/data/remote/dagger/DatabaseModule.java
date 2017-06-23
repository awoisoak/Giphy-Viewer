package com.awoisoak.giphyviewer.data.remote.dagger;

import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.data.local.dagger.DatabaseScope;
import com.awoisoak.giphyviewer.data.local.impl.SQLiteGifDataStore;

import java.sql.SQLException;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @DatabaseScope
    @Provides
    GifDataStore provideDataStore()  {
        return new SQLiteGifDataStore();
    }
}
