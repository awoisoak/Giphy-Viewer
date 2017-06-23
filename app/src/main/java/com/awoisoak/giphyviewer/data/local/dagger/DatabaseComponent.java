package com.awoisoak.giphyviewer.data.local.dagger;


import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.dagger.DatabaseModule;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiModule;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiScope;
import com.awoisoak.giphyviewer.presentation.ApplicationModule;

import dagger.Component;

@DatabaseScope
@Component(modules = {DatabaseModule.class, ApplicationModule.class})
public interface DatabaseComponent {
    GifDataStore getDataStore();
}
