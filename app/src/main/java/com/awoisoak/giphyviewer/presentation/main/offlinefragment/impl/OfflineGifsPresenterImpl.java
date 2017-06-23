package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;


import android.database.Cursor;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.presentation.main.MainActivity;
import com.awoisoak.giphyviewer.presentation.main.VisibleEvent;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;
//TODO Make the database request in threads?

public class OfflineGifsPresenterImpl implements OfflineGifsPresenter {
    public static String TAG = OfflineGifsPresenterImpl.class.getSimpleName();

    private OfflineGifsView mView;
    private DatabaseInteractor mInteractor;
    private List<Gif> mGifs = new ArrayList<>();

    private boolean isFirstRequest = true;
    private boolean mAllGifsRetrieved;

    private int mOffset;


    public OfflineGifsPresenterImpl(OfflineGifsView view, DatabaseInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "awooo | onCreate()");
        SignalManagerFactory.getSignalManager().register(this);
    }

    @Override
    public void onDestroy() {
        SignalManagerFactory.getSignalManager().unregister(this);
        mView = null;
    }


    /**
     * Retrieve the first gifs from the database
     */
    private void requestNewGifs() {
        //TODO Make the database request in threads?

        try {
            List<Gif> gifsRetrieved = mInteractor.getGifs(mOffset);
            if (gifsRetrieved.size() == 0){
                mView.showtoast("There is no gifs in the DB");
                return;
            }
            mGifs.addAll(gifsRetrieved);
            increaseOffset();

            if (isFirstRequest){
                mView.bindGifsList(mGifs);
                isFirstRequest = false;
            }else {
                mView.updateGifsList(gifsRetrieved);
            }
            //TODO somehow request the total records of the database or the cursos adapter will do the job?
//            if (mOffset >= response.getTotalRecords()) {
//                mAllGifsRetrieved = true;
//            }
//
        } catch (Exception e) {
            Log.e(TAG,"Exception trying to retrieve new gifs with offset = "+mOffset);
            mView.showtoast("Oops! There was an error retrieving the favourites gifs :-(");
        }
    }



    public void increaseOffset() {
        mOffset += GiphyApi.MAX_NUMBER_SEARCH_GIFS_RETURNED;
    }


    @Override
    public void onBottomReached() {
        System.out.println("awooooooo | OfflineGifsPresenterImpl | onBottomReached");
        if (mAllGifsRetrieved) {
            return;
        } else {
            requestNewGifs();
        }
    }

    @Override
    public void onGifSetAsFavourite(Gif gif) {
        System.out.println("awooooooo | OfflineGifsPresenterImpl | onGifSetAsFavourite");
    }





    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }

    /**
     * This method will be called when the fragment is visible for the user
     *
     * @param event
     */
    @Subscribe
    public void onVisibleEvent(final VisibleEvent event) {
        if (event.getPosition()== MainActivity.FAV_TAB){
            System.out.println("awooooooo | OfflineGifsPresenterImpl | onVisible");
            requestNewGifs();
        }
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onCreateView() {

    }

    @Override
    public void onActivityCreated() {

    }

    @Override
    public void onResume() {
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDetach() {

    }
}
