package com.awoisoak.giphyviewer.presentation.main.offlinefragment.impl;


import android.support.v4.app.Fragment;
import android.util.Log;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.GifDataStore;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.presentation.main.MainActivity;
import com.awoisoak.giphyviewer.presentation.main.VisibleEvent;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.offlinefragment.OfflineGifsView;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

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
        try {
            List<Gif> gifsRetrieved = mInteractor.getGifs(mOffset);
            if (mOffset == 0 && gifsRetrieved.size() == 0) {
                mView.bindGifsList(mGifs);
                mView.showtoast(((Fragment) mView).getString(R.string.empty_database));
                return;
            }
            mGifs.addAll(gifsRetrieved);
            increaseOffset();

            if (isFirstRequest) {
                mView.bindGifsList(mGifs);
                isFirstRequest = false;
            } else {
                mView.updateGifsList(gifsRetrieved);
            }
            if (mOffset >= mInteractor.getTotalNumberOfGifs()) {
                mAllGifsRetrieved = true;
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception trying to retrieve new gifs with offset = " + mOffset);
            mView.showtoast(((Fragment) mView).getString(R.string.error_retrieving_gifs_from_db));
        }
    }


    public void increaseOffset() {
        mOffset += GifDataStore.MAX_NUMBER_GIFS_RETURNED;
    }


    @Override
    public void onBottomReached() {
        if (mAllGifsRetrieved) {
            return;
        } else {
            requestNewGifs();
        }
    }

    @Override
    public void onUnsetGifAsFavourite(Gif gif) {
        if (mInteractor.removeGif(gif.getId())) {
            mGifs.remove(gif);
            mView.updateGifsList(mGifs);
        }
    }

    /**
     * This method will be called when the fragment is visible for the user
     *
     * @param event
     */
    @Subscribe
    public void onVisibleEvent(final VisibleEvent event) {
        if (event.getPosition() == MainActivity.FAV_TAB) {
            mAllGifsRetrieved = false;
            isFirstRequest = true;
            mOffset = 0;
            mGifs.clear();
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
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

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
