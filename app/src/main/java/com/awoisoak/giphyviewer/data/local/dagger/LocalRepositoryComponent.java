package com.awoisoak.giphyviewer.data.local.dagger;


import com.awoisoak.giphyviewer.data.LocalRepositoryModule;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.presentation.AppComponent;
import com.awoisoak.giphyviewer.presentation.ApplicationModule;

import dagger.Component;

@Component(modules = {LocalRepositoryModule.class, ApplicationModule.class})
public interface LocalRepositoryComponent extends AppComponent{
    LocalRepository getRepository();
}
