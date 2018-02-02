package com.awoisoak.giphyviewer.data.remote.dagger;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.RemoteRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class GiphyApiModule {

    @Provides
    GiphyApi provideGiphyApi() {
        return RemoteRepository.getInstance();
    }
}
