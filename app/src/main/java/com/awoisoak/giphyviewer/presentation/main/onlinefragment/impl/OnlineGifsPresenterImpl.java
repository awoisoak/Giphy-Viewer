package com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl;


import android.util.Log;
import android.view.View;

import com.awoisoak.giphyviewer.R;
import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
import com.awoisoak.giphyviewer.domain.interactors.DatabaseInteractor;
import com.awoisoak.giphyviewer.domain.interactors.GifsRequestInteractor;
import com.awoisoak.giphyviewer.presentation.main.MainActivity;
import com.awoisoak.giphyviewer.presentation.main.VisibleEvent;
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
    private GifsRequestInteractor mServerInteractor;
    private DatabaseInteractor mDatabaseInteractor;
    private List<Gif> mGifs = new ArrayList<>();
    private List<Gif> mFavouriteGifs = new ArrayList<>();

    private boolean isTrendingRequest = true;
    private boolean isFirstSearchRequest = true;
    private boolean mAllGifsDownloaded;
    private boolean mIsGifsRequestRunning;

    private int mOffset;


    public OnlineGifsPresenterImpl(OnlineGifsView view, GifsRequestInteractor serverInteractor,
                                   DatabaseInteractor databaseInteractor) {
        mView = view;
        mServerInteractor = serverInteractor;
        mDatabaseInteractor = databaseInteractor;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "awooo | onCreate()");
        SignalManagerFactory.getSignalManager().register(this);

    }

    @Override
    public void onCreateView() {
        Log.d(TAG, "awooo | onCreateView()");
        requestTrendingGifs();

    }

    @Override
    public void onDestroy() {
        SignalManagerFactory.getSignalManager().unregister(this);
        mView = null;
    }

    /**
     * Retrieve the trending gifs to be set as the default gifs in the recyclerView
     */
    private void requestTrendingGifs() {

        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mServerInteractor.getTrendingGifs();
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
            Log.d(TAG, "onGifsReceivedEvent | no gifs returned with query = " + mView.getSearchText());
            mView.showtoast(((OnlineGifsFragment) mView).getResources().getString(R.string.no_gifs_found));
            return;
        }

        //We make sure bind a new list against the adapter
        if (isFirstSearchRequest) {
            mGifs.clear();
        }

        //Add the new gifs to the array and increase the offset
        mGifs.addAll(response.getList());

        if (!isTrendingRequest) {
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
                    mAllGifsDownloaded = true;
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
        System.out.println("awoooo | OnlineGifsPresenterImple | requestToSearchNewGifs | mIsGifsRequestRunning=" +
                                   mIsGifsRequestRunning
                                   + "| isFirstSearchRequest = " + isFirstSearchRequest);
        if (!mIsGifsRequestRunning) {
            if (!isFirstSearchRequest) {
                mView.showLoadingSnackbar();
            }
            mIsGifsRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mServerInteractor.searchGifs(mView.getSearchText(), mOffset);
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
                    mServerInteractor.getTrendingGifs();
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
        mAllGifsDownloaded = false;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mServerInteractor.searchGifs(query, mOffset);
            }
        });
    }

    @Override
    public void onBottomReached() {
        System.out.println("awooooooo | OnlineGifsPresenterImpl | onBottomReached");
        if (mAllGifsDownloaded) {
            return;
        } else {
            requestToSearchNewGifs();
        }
    }

    @Override
    public void onGifSetAsFavourite(View v, Gif gif) {


        //TODO does the sync method really worth it??

        //TODO implement the ds.addGif to return a boolean as well to make sure that if the sql fails we can rely in the lists
        synchronized (mFavouriteGifs) {
            if (!isAlreadyFavourite(gif)) {
                mDatabaseInteractor.addGif(gif);
                mFavouriteGifs.add(gif);
            } else if (mDatabaseInteractor.removeGif(gif.getId())) {
                mFavouriteGifs.remove(gif);
            }
        }
    }

    @Override
    public boolean isAlreadyFavourite(Gif gif) {
        System.out.println("awooooooo | OnlineGifsPresenterImpl | isAlreadyFavourite = true");
        boolean wasAlreadyFavourite = false;
        //TODO does the sync method really worth it??

        synchronized (mFavouriteGifs) {
            for (Gif favGif : mFavouriteGifs) {
                if (favGif.getId().equals(gif.getId())) {
                    wasAlreadyFavourite = true;
                }
            }
        }
        return wasAlreadyFavourite;
    }


    /**
     * This method will be called when the fragment is visible for the user
     *
     * @param event
     */
    @Subscribe
    public void onVisibleEvent(final VisibleEvent event) {
        synchronized (mFavouriteGifs) {
            if (event.getPosition() == MainActivity.SEARCH_TAB) {
                System.out.println("awooooooo | OnlineGifsPresenterImpl | onVisible");
                mFavouriteGifs = mDatabaseInteractor.getAllGifs();
                // We force the rv to be updated with the possible changes in the offline screen
                mView.updateGifsList(mGifs);
            }
        }
    }


    @Override
    public void onAttach() {

    }

    @Override
    public void onActivityCreated() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {
        System.out.println("awooooo | OnlineGifsPresenterImpl | onResume");
        /**
         * Workaround to detect the very first time the online screen is displayed
         * (OnPageChangeListener can't detect it)
         */
        onVisibleEvent(new VisibleEvent(MainActivity.SEARCH_TAB));
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onDetach() {

    }
}
