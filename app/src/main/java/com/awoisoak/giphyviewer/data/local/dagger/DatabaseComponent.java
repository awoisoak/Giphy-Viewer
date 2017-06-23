package com.awoisoak.giphyviewer.data.local.dagger;


import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.presentation.AppComponent;
import com.awoisoak.giphyviewer.presentation.ApplicationModule;

import dagger.Component;
@Component(modules = {DatabaseModule.class, ApplicationModule.class})
public interface DatabaseComponent extends AppComponent {
    GifDataStore getDataStore();
}
