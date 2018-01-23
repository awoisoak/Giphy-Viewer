package com.awoisoak.giphyviewer.data;

import com.awoisoak.giphyviewer.data.local.impl.GifDao;
import com.awoisoak.giphyviewer.data.local.impl.GifDatabase;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    Repository provideRepository() {
        GifDao gifDao= GifDatabase.getInstance(GiphyViewerApplication.getGiphyViewerApplication()).gifDao();
        return Repository.getInstance(gifDao);
    }
}
