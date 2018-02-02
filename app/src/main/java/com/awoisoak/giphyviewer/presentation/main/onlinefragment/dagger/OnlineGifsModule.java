package com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger;


import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.remote.RemoteRepository;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsView;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModel;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineGifsModule {
    private final OnlineGifsView mView;

    public OnlineGifsModule(OnlineGifsView view) {
        mView = view;
    }

//    @Provides
//    @ActivityScope
//    GifsRequestInteractor provideGifsRequestInteractor(GiphyApi2 api) {
//        return new GifsRequestInteractorImpl(api);
//    }


//    @Provides
//    @ActivityScope
//    OnlineGifsPresenter provideGifsOnlinePresenter(GifsRequestInteractor serverInteractor, LocalRepository localRepository) {
//        return new OnlineGifsPresenterImpl(mView, serverInteractor,localRepository);
//    }

    @Provides
    @ActivityScope
    OnlineViewModel provideGifsOnlineViewModel(LocalRepository localRepository,RemoteRepository remoteRepository) {
        return new OnlineViewModelFactory(localRepository,remoteRepository).create(OnlineViewModel.class);
    }
}
