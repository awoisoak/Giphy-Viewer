package com.awoisoak.giphyviewer.presentation;

import android.app.Application;

import com.awoisoak.giphyviewer.data.remote.dagger.DaggerGiphyApiComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiComponent;

import dagger.Module;

@Module
public class GiphyViewerApplication extends Application {
    private GiphyApiComponent mGiphyApiComponent;
    private static GiphyViewerApplication sGiphyViewerApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        sGiphyViewerApplication = this;
        mGiphyApiComponent =
                DaggerGiphyApiComponent.builder()
                        .applicationModule(new ApplicationModule(getApplicationContext())).build();
    }

    public GiphyApiComponent getWPAPIComponent() {
        return mGiphyApiComponent;
    }


    public static GiphyViewerApplication getVisorApplication() {
        if (sGiphyViewerApplication == null) {
            throw new NullPointerException("GiphyViewerApplication is null");
        } else {
            return sGiphyViewerApplication;
        }
    }
}
