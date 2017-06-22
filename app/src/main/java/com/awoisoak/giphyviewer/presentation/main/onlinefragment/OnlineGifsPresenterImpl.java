package com.awoisoak.giphyviewer.presentation.main.onlinefragment;


import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.responses.ListGifsResponse;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
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
     * //TODO This will be called for both trending and search gifs. Check whether we should split them
     *
     * @param response
     */
    @Subscribe
    public void onGifsReceivedEvent(final ListGifsResponse response) {
        Log.d(TAG, "@BUS | onGifsReceivedEvent | response | code = " + response.getCode());

        if (response.getList().size() == 0) {
            Log.e(TAG, "onGifsReceivedEvent = 0??");
            return;
        }

        //Add the new gifs to the array and increase the offset
        mGifs.addAll(response.getList());
        increaseOffset();

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
        mOffset += GiphyApi.MAX_NUMBER_GIFS_RETURNED;
    }

    @Override
    public void onSearchSubmitted(final String query) {
        //Is a new search so we clear the offset
        mOffset = 0;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mInteractor.searchGifs(query, mOffset);
            }
        });
    }

    @Override
    public void onBottomReached() {
        if (mAllPostsDownloaded) {
            return;
        } else {
            requestToSearchNewGifs();
        }
    }

    @Override
    public void onGifSetAsFavourite(boolean favourite) {
        //TODO how to manage the favourite feature?
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
