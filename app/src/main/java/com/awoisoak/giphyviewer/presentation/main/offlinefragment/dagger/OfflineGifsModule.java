//package com.awoisoak.giphyviewer.presentation.main.offlinefragment.dagger;
//
//
//import com.awoisoak.giphyviewer.data.local.GifDataStore;
//import com.awoisoak.giphyviewer.domain.interactors.DataBaseInteractor;
//import com.awoisoak.giphyviewer.domain.interactors.impl.DatabaseInteractorImpl;
//import com.awoisoak.giphyviewer.presentation.ActivityScope;
//import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsPresenter;
//
//import dagger.Module;
//import dagger.Provides;
//
//@Module
//public class OfflineGifsModule {
//    private final OfflineGifsView mView;
//
//    public OfflineGifsModule(OfflineGifsView view) {
//        mView = view;
//    }
//
//    @Provides
//    @ActivityScope
//    DataBaseInteractor provideDatabaseInteractor(GifDataStore ds) {
//        return new DatabaseInteractorImpl(ds);
//    }
//
//    @Provides
//    @ActivityScope
//    OnlineGifsPresenter provideGifsOfflinePresenter(DataBaseInteractor interactor) {
//        return new OfflineGifsPresenterImpl(mView, interactor);
//    }
//}