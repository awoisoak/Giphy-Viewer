package com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger;


import com.awoisoak.giphyviewer.data.RepositoryComponent;
import com.awoisoak.giphyviewer.presentation.ActivityScope;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl.OfflineGifsFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = RepositoryComponent.class, modules = OfflineGifsModule.class)
public interface OfflineGifsComponent {
    /**
     * It will inject the GifsOfflinePresenter returned in GifsOfflineModule
     * in the variable with the @Inject annotation in GifsOfflineFragment
     * (This method could have any other name, it will just says that will inject the dependencies given in GifsOfflineModule
     * into the variables with the @Inject annotation in GifsOfflineFragment)
     */
    void inject(OfflineGifsFragment o);
}
