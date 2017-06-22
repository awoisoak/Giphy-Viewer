package com.awoisoak.giphyviewer.presentation.main.fragment1.dagger;


import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.domain.interactors.impl.GifsRequestInteractorImpl;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.fragment1.GifsOnlinePresenter;
import com.awoisoak.giphyviewer.presentation.main.fragment1.GifsOnlinePresenterImpl;
import com.awoisoak.giphyviewer.presentation.main.fragment1.GifsOnlineView;

import dagger.Module;
import dagger.Provides;

@Module
public class GifsOnlineModule {
    private final GifsOnlineView mView;

    public GifsOnlineModule(GifsOnlineView view) {
        mView = view;
    }

    @Provides
    @ActivityScope
    GifsRequestInteractor provideGifsRequestInteractor(GiphyApi api) {
        return new GifsRequestInteractorImpl(api);
    }

    @Provides
    @ActivityScope
    GifsOnlinePresenter provideGifsOnlinePresenter(GifsRequestInteractor interactor) {
        return new GifsOnlinePresenterImpl(mView, interactor);
    }
}
