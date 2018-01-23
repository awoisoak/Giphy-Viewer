package com.awoisoak.giphyviewer.data;


import com.awoisoak.giphyviewer.presentation.AppComponent;
import com.awoisoak.giphyviewer.presentation.ApplicationModule;

import dagger.Component;

@Component(modules = {RepositoryModule.class, ApplicationModule.class})
public interface RepositoryComponent extends AppComponent{
    Repository getRepository();
}
