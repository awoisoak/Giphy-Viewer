package com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger;


import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.domain.interactors.impl.DatabaseInteractorImpl;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl.OfflineGifsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class OfflineGifsModule {
    private final OfflineGifsView mView;

    public OfflineGifsModule(OfflineGifsView view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    DatabaseInteractor provideDatabaseInteractor(GifDataStore ds) {
        return new DatabaseInteractorImpl(ds);
    }

    @Provides
    @ActivityScope
    OfflineGifsPresenter provideGifsOfflinePresenter(DatabaseInteractor interactor) {
        return new OfflineGifsPresenterImpl(mView, interactor);
    }
}
