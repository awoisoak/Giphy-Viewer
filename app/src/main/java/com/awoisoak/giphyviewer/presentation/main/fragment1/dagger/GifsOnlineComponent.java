package com.awoisoak.giphyviewer.presentation.main.fragment1.dagger;


import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiComponent;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.fragment1.GifsOnlineFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = GiphyApiComponent.class, modules = GifsOnlineModule.class)
public interface GifsOnlineComponent {
    /**
     * It will inject the GifsOnlinePresenter returned in GifsOnlineModule
     * in the variable with the @Inject annotation in GifsOnlineFragment
     * (This method could have any other name, it will just says that will inject the dependencies given in GifsOnlineModule
     * into the variables with the @Inject annotation in GifsOnlineFragment)
     */
    void inject(GifsOnlineFragment o);
}
