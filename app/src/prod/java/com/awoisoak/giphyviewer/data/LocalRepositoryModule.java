package com.awoisoak.giphyviewer.data;

import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.local.impl.GifDao;
import com.awoisoak.giphyviewer.data.local.impl.GifDatabase;
import com.awoisoak.giphyviewer.presentation.GiphyViewerApplication;

import dagger.Module;
import dagger.Provides;

@Module
public class LocalRepositoryModule {

    @Provides
    GifDao provideGifDao() {
        return GifDatabase.getInstance(GiphyViewerApplication.getGiphyViewerApplication()).gifDao();
    }

    @Provides
    LocalRepository provideRepository() {
        return LocalRepository.getInstance(provideGifDao());
    }
}
