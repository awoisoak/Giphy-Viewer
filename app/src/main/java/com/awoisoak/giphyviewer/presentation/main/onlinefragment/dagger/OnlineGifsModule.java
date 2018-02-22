package com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger;


import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.remote.RemoteRepository;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModel;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineViewModelFactory;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl.OnlineGifsFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class OnlineGifsModule {
    private final OnlineGifsFragment mView;

    public OnlineGifsModule(OnlineGifsFragment view) {
        mView = view;
    }


    @Provides
    @ActivityScope
    OnlineViewModel provideGifsOnlineViewModel(LocalRepository localRepository,RemoteRepository remoteRepository) {
        return new OnlineViewModelFactory(localRepository,remoteRepository).create(OnlineViewModel.class);
    }
}
