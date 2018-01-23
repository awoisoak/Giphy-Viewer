package com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger;


import com.awoisoak.giphyviewer.data.Repository;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.domain.interactors.impl.DatabaseInteractorImpl;
import com.awoisoak.giphyviewer.domain.interactors.impl.GifsRequestInteractorImpl;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.AppScope;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl.OnlineGifsPresenterImpl;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsView;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineGifsModule {
    private final OnlineGifsView mView;

    public OnlineGifsModule(OnlineGifsView view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    GifsRequestInteractor provideGifsRequestInteractor(GiphyApi api) {
        return new GifsRequestInteractorImpl(api);
    }

    @Provides
    @ActivityScope
    DatabaseInteractor provideDatabaseInteractor(Repository repository) {
        return new DatabaseInteractorImpl(repository);
    }

    @Provides
    @ActivityScope
    OnlineGifsPresenter provideGifsOnlinePresenter(GifsRequestInteractor serverInteractor, DatabaseInteractor databaseInteractor) {
        return new OnlineGifsPresenterImpl(mView, serverInteractor,databaseInteractor);
    }
}
