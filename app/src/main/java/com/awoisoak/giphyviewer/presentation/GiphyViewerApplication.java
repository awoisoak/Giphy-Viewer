package com.awoisoak.giphyviewer.presentation;

import android.app.Application;

import com.awoisoak.giphyviewer.data.local.dagger.DaggerDatabaseComponent;
import com.awoisoak.giphyviewer.data.local.dagger.DatabaseComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.DaggerGiphyApiComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiComponent;

import dagger.Module;

@Module
public class GiphyViewerApplication extends Application {
    private DatabaseComponent mDatabaseComponent;
    private GiphyApiComponent mGiphyApiComponent;
    private static GiphyViewerApplication sGiphyViewerApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        sGiphyViewerApplication = this;
        mGiphyApiComponent =
                DaggerGiphyApiComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
        mDatabaseComponent =
                DaggerDatabaseComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public GiphyApiComponent getGiphyApiComponent() {
        return mGiphyApiComponent;
    }

    public DatabaseComponent getDatabaseComponent() {
        return mDatabaseComponent;
    }


    public static GiphyViewerApplication getGiphyViewerApplication() {
        if (sGiphyViewerApplication == null) {
            throw new NullPointerException("GiphyViewerApplication is null");
        } else {
            return sGiphyViewerApplication;
        }
    }
}
