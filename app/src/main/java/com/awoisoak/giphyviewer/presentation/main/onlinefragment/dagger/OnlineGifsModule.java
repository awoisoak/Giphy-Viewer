package com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger;


import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.domain.interactors.impl.GifsRequestInteractorImpl;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
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
    OnlineGifsPresenter provideGifsOnlinePresenter(GifsRequestInteractor interactor) {
        return new OnlineGifsPresenterImpl(mView, interactor);
    }
}
