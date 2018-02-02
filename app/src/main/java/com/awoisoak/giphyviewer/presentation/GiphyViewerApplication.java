package com.awoisoak.giphyviewer.presentation;

import android.app.Application;

import com.awoisoak.giphyviewer.data.local.dagger.DaggerLocalRepositoryComponent;
import com.awoisoak.giphyviewer.data.local.dagger.LocalRepositoryComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.DaggerGiphyApiComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiComponent;

import dagger.Module;

@Module
public class GiphyViewerApplication extends Application {
    private LocalRepositoryComponent mLocalRepositoryComponent;
    private GiphyApiComponent mGiphyApiComponent;
    private static GiphyViewerApplication sGiphyViewerApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        sGiphyViewerApplication = this;
        mGiphyApiComponent =
                DaggerGiphyApiComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
        mLocalRepositoryComponent =
                DaggerLocalRepositoryComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public GiphyApiComponent getGiphyApiComponent() {
        return mGiphyApiComponent;
    }

    public LocalRepositoryComponent getLocalRepositoryComponent() {
        return mLocalRepositoryComponent;
    }


    public static GiphyViewerApplication getGiphyViewerApplication() {
        if (sGiphyViewerApplication == null) {
            throw new NullPointerException("GiphyViewerApplication is null");
        } else {
            return sGiphyViewerApplication;
        }
    }
}
