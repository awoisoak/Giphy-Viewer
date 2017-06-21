package com.awoisoak.giphyviewer.data.remote.dagger;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.GiphyManager;

import dagger.Module;
import dagger.Provides;

@Module
public class GiphyApiModule {

    @GiphyApiScope
    @Provides
    GiphyApi provideGiphyApi() {
        return GiphyManager.getInstance();
    }
}
