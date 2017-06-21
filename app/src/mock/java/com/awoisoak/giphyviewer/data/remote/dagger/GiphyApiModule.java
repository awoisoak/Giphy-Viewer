package com.awoisoak.giphyviewer.data.remote.dagger;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.MockGiphyManager;


import dagger.Module;
import dagger.Provides;

@Module
public class GiphyApiModule {

    @GiphyApiScope
    @Provides
    GiphyApi provideGiphyAPI() {
        return MockGiphyManager.getInstance();
    }
}
