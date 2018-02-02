package com.awoisoak.giphyviewer.presentation.main.onlinefragment;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.Resource;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.GiphyResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awo on 1/31/18.
 */

public class OnlineViewModel extends ViewModel {
    public static String TAG = OnlineViewModel.class.getSimpleName();

    LocalRepository mLocalRepository;
    GiphyApi mRemoteRepository;
    private MutableLiveData<Resource<List<Gif>>> mSearchedGifs = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Gif>>> mTrendingGifs = new MutableLiveData<>();
    private List<Gif> mFavouriteGifs = new ArrayList<>();
    private static final Object LOCK = new Object();
    private boolean isFirstSearchRequest = true;
    private boolean mAllGifsDownloaded;
    private boolean mIsGifsRequestRunning;
    private int mOffset;
    private final List<Gif> emptyList = new ArrayList<>();
    private boolean isTrendingRequest = true;
    private String lastquery;

    public OnlineViewModel(LocalRepository localRepository, GiphyApi remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository =remoteRepository;
        SignalManagerFactory.getSignalManager().register(this);
        observeDatabase();
        observeNetwork();

        requestTrendingGifs();
    }

    //TODO remove the observerForever methods. See if the observation should be done in the UI,
    // or u should use transformations
    private void observeDatabase() {
        mLocalRepository.getAllGifs().observeForever(new Observer<List<Gif>>() {
            @Override
            public void onChanged(@Nullable List<Gif> gifsFromDB) {
                System.out.println(
                        "awooooo | onlineGifsPresenterImpl | observeDb | onChanged | size="
                                + gifsFromDB.size());
                mFavouriteGifs = gifsFromDB;
            }
        });
    }

    public MutableLiveData<Resource<List<Gif>>> getSearchedGifs() {
        return mSearchedGifs;
    }

    public MutableLiveData<Resource<List<Gif>>> getTrendingGifs() {
        return mTrendingGifs;
    }


    /**
     * Here in ViewModel we probably shouldn't really need the Resource wrapper to have a
     * status of  the data as we already have a structure of classes
     * (ErrorResponse & ListGifsResponse) than indicates the status of the data
     * and extend from the same father class (GiphyResponse).
     * I use the Resource paradigm for educational purposes of the best
     * practices.
     *
     * In any case we needed to expose a Resource (data with a status associated) to the UI so it
     * can behave properly depending on the data status. We don't expose the GiphyResponses to the UI
     * but the 'real' data itself.
     */
    private void observeNetwork() {
        mRemoteRepository.getSearchGifs().observeForever(
                new Observer<Resource<GiphyResponse>>() {
                    @Override
                    public void onChanged(@Nullable Resource<GiphyResponse> resource) {

                        System.out.println(
                                "awoooooo | OnlineGifsFragment | observeNetwork | onChanged | "
                                        + "number of Gifs = "
                                        + ((ListGifsResponse) resource.data).getList().size());
                        switch (resource.status) {
                            case SUCCESS:
                                onGifsReceived((ListGifsResponse) resource.data,
                                        resource.data.getTYPE());
                                break;
                            case ERROR:
                                onErrorRetrievingGifs((ErrorResponse) resource.data,
                                        resource.data.getTYPE());
                                break;
                            default:
                                Log.e(TAG, "Unknown Resource Status");
                        }
                    }
                });
    }

    private void onGifsReceived(ListGifsResponse response, GiphyApi.REQUEST_TYPE type) {

        switch (type) {
            case SEARCH:
                isTrendingRequest = false;
                //We make sure bind a new list against the adapter
                //TODO is this really needed?
                if (isFirstSearchRequest) {
                    mSearchedGifs.postValue(Resource.loading(emptyList));
                    isFirstSearchRequest = false;
                }
                //TODO should we keep all the values in mSearchedGifs?
                //TODO By now we just postValue the last retrieved
                //Add the new gifs to the array and increase the offset
                //        List<Gif> tmp = mSearchedGifs.getValue();
                //        tmp.addAll(response.getList());
                //        mSearchedGifs.postValue(tmp);
                mSearchedGifs.postValue(Resource.success(response.getList()));
                increaseOffset();
                mIsGifsRequestRunning = false;
                if (mOffset >= response.getTotalRecords()) {
                    mAllGifsDownloaded = true;
                }
                break;

            case TRENDING:
                mTrendingGifs.postValue(Resource.success(response.getList()));
                break;
            default:
                Log.e(TAG, "Request type unknown");
        }
    }

    //TODO The UI might need to observer the Resource object itself
    //TODO We might want to pass to UI a Resource<List<Gifs>> instead of a
    // Resource<ListGifsResponse>>
    private void onErrorRetrievingGifs(ErrorResponse data, GiphyApi.REQUEST_TYPE type) {
        List<Gif> emptyList = new ArrayList<>();
        switch (type) {
            case SEARCH:
                mSearchedGifs.postValue(Resource.error(data.getMessage(), emptyList));
                break;
            case TRENDING:
                mTrendingGifs.postValue(Resource.error(data.getMessage(), emptyList));
                break;
            default:
                Log.e(TAG, "Request type unknown");

                mIsGifsRequestRunning = false;//TODO we need it?


        }
    }

        /**
         * Retrieve the trending gifs to be set as the default gifs in the recyclerView
         */

    private void requestTrendingGifs() {
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mRemoteRepository.getTrendingGifs();
            }
        });
    }


    private void requestToSearchNewGifs() {
        if (!mIsGifsRequestRunning) {
            if (!isFirstSearchRequest) {
                mSearchedGifs.postValue(Resource.loading(emptyList));
            }
            mIsGifsRequestRunning = true;
            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    mRemoteRepository.search(lastquery, mOffset);
                }
            });
        }
    }


    public void onRetryGifsRequest() {
        if (isTrendingRequest) {
            requestTrendingGifs();
        } else {
            requestToSearchNewGifs();
        }
    }


    public void increaseOffset() {
        mOffset += GiphyApi.MAX_NUMBER_SEARCH_GIFS_RETURNED;
    }

    public void onSearchSubmitted(final String query) {
        mOffset = 0;
        isFirstSearchRequest = true;
        mAllGifsDownloaded = false;
        lastquery = query;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                mRemoteRepository.search(query, mOffset);
            }
        });
    }

    public void onBottomReached() {
        if (mAllGifsDownloaded) {
            return;
        } else {
            requestToSearchNewGifs();
        }
    }


    public void onGifSetAsFavourite(final Gif gif) {

        //TODO implement the ds.addGif to return a boolean as well to make sure that if the sql
        // fails we can rely in the lists
        synchronized (LOCK) {

            ThreadPool.run(new Runnable() {
                @Override
                public void run() {
                    if (!isAlreadyFavourite(gif)) {
                        mLocalRepository.addGif(gif);
//                    mFavouriteGifs.add(gif);
                    } else if (mLocalRepository.removeGif(gif.getId()) > 0) {
//                    mFavouriteGifs.remove(gif);
                    }
                }
            });

        }
    }


    public boolean isAlreadyFavourite(Gif gif) {
        boolean wasAlreadyFavourite = false;

        synchronized (LOCK) {
            for (Gif favGif : mFavouriteGifs) {
                if (favGif.getId() == (gif.getId())) {
                    wasAlreadyFavourite = true;
                }
            }
        }
        return wasAlreadyFavourite;
    }

//TODO Do we need this at all?
//    /**
//     * This method will be called when the fragment is visible for the user
//     */
//    @Subscribe
//    public void onVisibleEvent(final VisibleEvent event) {
//        synchronized (LOCK) {
//            if (event.getPosition() == MainActivity.SEARCH_TAB) {
//                ThreadPool.run(new Runnable() {
//                    @Override
//                    public void run() {
////                        // Not needed as we are already observing all data?
////                        mLocalRepository.getAllGifs();
//
//
//                        ThreadPool.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                // We force the recyclerView to be updated with the possible
//                                // changes in the offline screen
//                                mView.updateGifsList(mSearchedGifs);
//                            }
//                        });
//                    }
//                });
//            }
//        }
//    }


    @Override
    protected void onCleared() {
        super.onCleared();
        SignalManagerFactory.getSignalManager().unregister(this);

    }

}