package com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger;


import com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl.OfflineGifsFragment;

import dagger.Module;

@Module
public class OfflineGifsModule {
    private final OfflineGifsFragment mView;

    public OfflineGifsModule(OfflineGifsFragment view) {
        mView = view;
    }


}
