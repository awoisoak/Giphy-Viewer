package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;


import android.database.Cursor;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
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
        ///////////////////
        //TODO only for testing
        try {
            Gif gif;

            gif = new Gif(Integer.toString(1), "https://media1.giphy.com/media/5zGPIuq3IOfXG/giphy.gif",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(2), "https://media4.giphy.com/media/MuuYIotTu3GG4/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(3), "https://media2.giphy.com/media/isP4TLqhjm3zq/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(4), "https://media0.giphy.com/media/E0MM3wqMMCQN2/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(5), "https://media2.giphy.com/media/isP4TLqhjm3zq/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(6), "https://media0.giphy.com/media/E0MM3wqMMCQN2/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(7), "https://media2.giphy.com/media/isP4TLqhjm3zq/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(8), "https://media0.giphy.com/media/E0MM3wqMMCQN2/200.gif?response_id=594cc8f128668855925445c9",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(9), "https://media2.giphy.com/media/isP4TLqhjm3zq/200.gif?response_id=594ccaa094ba92f87cade4b2",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(10), "https://media0.giphy.com/media/E0MM3wqMMCQN2/200.gif?response_id=594ccaa094ba92f87cade4b2",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(11), "https://media1.giphy.com/media/Mvw4JCwtu0FO/200.gif?response_id=594ccaa094ba92f87cade4b2",false);
            mInteractor.addGif(gif);
            gif = new Gif(Integer.toString(12), "https://media4.giphy.com/media/3oz8xQQ7zuKYvbTvXy/200.gif?response_id=594ccaa094ba92f87cade4b2",false);
            mInteractor.addGif(gif);


        } catch (Exception e) {
            e.printStackTrace();
        }


        ///////////////////
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
            if (mOffset == 0 && gifsRetrieved.size() == 0){
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
            //TODO somehow request the total records of the database or the cursor adapter will do the job?
            if (mOffset >= mInteractor.getTotalNumberOfGifs()) {
                mAllGifsRetrieved = true;
            }

        } catch (Exception e) {
            Log.e(TAG,"Exception trying to retrieve new gifs with offset = "+mOffset);
            mView.showtoast("Oops! There was an error retrieving the favourites gifs :-(");
        }
    }



    public void increaseOffset() {
        mOffset += GifDataStore.MAX_NUMBER_GIFS_RETURNED;
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
    public void onUnsetGifAsFavourite(Gif gif) {
        System.out.println("awooooooo | OfflineGifsPresenterImpl | onUnsetGifAsFavourite");
            mInteractor.removeGif(gif.getId());
        mGifs.remove(gif);
        mView.updateGifsList(mGifs);
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
