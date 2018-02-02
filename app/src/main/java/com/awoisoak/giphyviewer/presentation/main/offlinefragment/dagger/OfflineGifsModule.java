package com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger;


import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;

import dagger.Module;

@Module
public class OfflineGifsModule {
    private final OfflineGifsView mView;

    public OfflineGifsModule(OfflineGifsView view) {
        mView = view;
    }

//    @Provides
//    @ActivityScope
//    OfflineViewModel provideGifsOfflineViewModel(LocalRepository localRepository) {
//        return new OfflineViewModelFactory(localRepository).create(OfflineViewModel.class);
//    }
}
