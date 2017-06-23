package com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl;


import android.util.Log;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsPresenter;
import com.awoisoak.giphyviewer.presentation.main.onlinefragment.OnlineGifsView;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class OnlineGifsPresenterImpl implements OnlineGifsPresenter {
    public static String TAG = OnlineGifsPresenterImpl.class.getSimpleName();

    private OnlineGifsView mView;
    private GifsRequestInteractor mInteractor;
    private List<Gif> mGifs = new ArrayList<>();

    private boolean isTrendingRequest = true;
    private boolean isFirstSearchRequest = true;
    private boolean mAllPostsDownloaded;
    private boolean mIsGifsRequestRunning;

    private int mOffset;


    public OnlineGifsPresenterImpl(OnlineGifsView view, GifsRequestInteractor interactor) {
        mView = view;
        mInteractor = interactor;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "awooo | onCreate()");
        SignalManagerFactory.getSignalManager().register(this);
        requestTrendingGifs();

    }

    /**
     * Retrieve the trending gifs to be set as the default gifs in the recyclerView
     */
    private void requestTrendingGifs() {

        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.getTrendingGifs();
            }
        });
    }


    /**
     * This method will be called when the interactor returns the new gifs
     *
     * @param response
     */
    @Subscribe
    public void onGifsReceivedEvent(final ListGifsResponse response) {
        Log.d(TAG, "@BUS | onGifsReceivedEvent | response | code = " + response.getCode());

        if (response.getList().size() == 0) {
            Log.d(TAG, "onGifsReceivedEvent | no gifs returned with query = "+mView.getSearchText());
            mView.showtoast(((OnlineGifsFragment) mView).getResources().getString(R.string.no_gifs_found));
            return;
        }

        //We make sure bind a new list against the adapter
        if (isFirstSearchRequest) {
            mGifs.clear();
        }

        //Add the new gifs to the array and increase the offset
        mGifs.addAll(response.getList());

        if (!isTrendingRequest){
            increaseOffset();
        }

        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideSnackbar();
                mView.hideProgressBar();
                if (isTrendingRequest) {
                    mView.bindGifsList(mGifs);
                } else if (isFirstSearchRequest) {
                    mView.bindGifsList(mGifs);
                    isFirstSearchRequest = false;
                } else {
                    mView.updateGifsList(response.getList());
                    mView.hideSnackbar();
                }
                mIsGifsRequestRunning = false;
                if (mOffset >= response.getTotalRecords()) {
                    mAllPostsDownloaded = true;
                }
                isTrendingRequest = false;
            }
        });
    }

    /**
     * This method will be called when the interactor returns an error trying to get the new gifs
     *
     * @param response
     */
    @Subscribe
    public void onErrorRetrievingGifsEvent(ErrorResponse response) {
        Log.d(TAG, "@BUS | onErrorRetrievingGifs | response | code = " + response.getCode());

        mIsGifsRequestRunning = false;
        ThreadPool.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mView.hideProgressBar();
                mView.showErrorSnackbar();
            }
        });
    }


    private void requestToSearchNewGifs() {
        System.out.println("awoooo | OnlineGifsPresenterImple | requestToSearchNewGifs | mIsGifsRequestRunning="+mIsGifsRequestRunning
        +"| isFirstSearchRequest = "+isFirstSearchRequest);
        if (!mIsGifsRequestRunning) {
            if (!isFirstSearchRequest) {
                mView.showLoadingSnackbar();
            }
            mIsGifsRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mInteractor.searchGifs(mView.getSearchText(), mOffset);
                }
            });
        }
    }

    @Override
    public void onRetryTrendingGifsRequest() {
        //TODO We need to call this when the trending request fails
        if (!mIsGifsRequestRunning) {
            if (!isFirstSearchRequest) {
                mView.showLoadingSnackbar();
            }
            mIsGifsRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mInteractor.getTrendingGifs();
                }
            });
        }
    }

    @Override
    public void onRetrySearchGifsRequest() {
        requestToSearchNewGifs();
    }

    public void increaseOffset() {
        mOffset += GiphyApi.MAX_NUMBER_SEARCH_GIFS_RETURNED;
    }

    @Override
    public void onSearchSubmitted(final String query) {
        mOffset = 0;
        isFirstSearchRequest = true;
        mAllPostsDownloaded = false;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.searchGifs(query, mOffset);
            }
        });
    }

    @Override
    public void onBottomReached() {
        System.out.println("awooooooo | OnlineGifsPresenterImpl | onBottomReached");
        if (mAllPostsDownloaded) {
            return;
        } else {
            requestToSearchNewGifs();
        }
    }

    @Override
    public void onGifSetAsFavourite(Gif gif) {
        System.out.println("awooooooo | OnlineGifsPresenterImpl | onGifSetAsFavourite");
    }


    @Override
    public void onDestroy() {
        SignalManagerFactory.getSignalManager().unregister(this);
        mView = null;
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
