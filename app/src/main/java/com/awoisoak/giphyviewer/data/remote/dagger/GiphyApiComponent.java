package com.awoisoak.giphyviewer.data.remote.dagger;


import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.presentation.ApplicationModule;

import dagger.Component;

@GiphyApiScope
@Component(modules = {GiphyApiModule.class, ApplicationModule.class})
public interface GiphyApiComponent {
    GiphyApi getGiphyApi();
}
