package com.awoisoak.giphyviewer.presentation.main.fragment1;


import android.util.Log;

import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;

public class GifsOnlinePresenterImpl implements GifsOnlinePresenter {
    public static String TAG = GifsOnlinePresenterImpl.class.getSimpleName();


    private GifsOnlineView mView;
    private GifsRequestInteractor mInteractor;

    public GifsOnlinePresenterImpl(GifsOnlineView view, GifsRequestInteractor interactor) {
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"awooo | onCreate()");

    }
    @Override
    public void onRetryGifsRequest() {

    }

    @Override
    public void onBottomReached() {

    }

    @Override
    public void onGifSetAsFavourite(boolean favourite) {

    }


    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }
}
