package com.awoisoak.giphyviewer.presentation.main.onlinefragment.dagger;


import com.awoisoak.giphyviewer.data.RepositoryComponent;
import com.awoisoak.giphyviewer.data.remote.dagger.GiphyApiComponent;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl.OnlineGifsFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = {GiphyApiComponent.class, RepositoryComponent.class}, modules = OnlineGifsModule.class)
public interface OnlineGifsComponent {
    /**
     * It will inject the GifsOnlinePresenter returned in GifsOnlineModule
     * in the variable with the @Inject annotation in GifsOnlineFragment
     * (This method could have any other name, it will just says that will inject the dependencies given in GifsOnlineModule
     * into the variables with the @Inject annotation in GifsOnlineFragment)
     */
    void inject(OnlineGifsFragment o);
}
