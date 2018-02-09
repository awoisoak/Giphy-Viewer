package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.presentation.main.MainActivity;
import com.awoisoak.giphyviewer.presentation.main.VisibleEvent;
import com.awoisoak.giphyviewer.utils.signals.SignalManagerFactory;
import com.awoisoak.giphyviewer.utils.threading.ThreadPool;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by awo on 1/24/18.
 */
//TODO request new gifs do really make any sense? seems like we are retrieving all the gifs as once

public class OfflineViewModel extends ViewModel {
    private static final Object LOCK = new Object();
    public static String TAG = "awoooo" + OfflineViewModel.class.getSimpleName();
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Gif>> mObservableGifs;
    private Observer mObserver;
    private boolean isFirstRequest = true;
    private boolean mAllGifsRetrieved;
    private int mOffset;
    private LocalRepository mLocalRepository;
    private int mLastTotalNumberOfGifs;
    private Gif mLastGifRemoved;
    private boolean requestingNewGifs;


    public OfflineViewModel(LocalRepository localRepository) {
        SignalManagerFactory.getSignalManager().register(this);
        mLocalRepository = localRepository;
        mObservableGifs = new MediatorLiveData<>();
        observeDB();
        requestNewGifs();
//        retrieveAllGifs();
    }

    private void observeDB() {

        mObserver = new Observer<List<Gif>>() {
            @Override
            public void onChanged(@Nullable final List<Gif> gifs) {
                Log.d(TAG, "awooo Generic Observer | onchanged | size = " + gifs.size());
                synchronized (LOCK) {
                    ThreadPool.run(new Runnable() {
                        @Override
                        public void run() {
                            //TODO The main problem seems to be that we are having multiple
                            // events in
                            // the observer
                            //TODO Seems to experience this problem only when new gifs are requested
                            //as we are adding new sources to the generic observer
                            ////////////////////////


                            // 1) A Gif has been removed
                            if (mLocalRepository.getTotalNumberOfGifs() < mLastTotalNumberOfGifs) {
                                Log.d(TAG, "postValue | gif removed ");
                                List<Gif> tmp = getListAfterGifRemoved();
                                mObservableGifs.postValue(tmp);
                                //TODO this leaves mOffset = -1 at some point. Fix it.
                                decreaseOffsetBy1();
                            }
                            // 2) First Request to the DB or Gif added from onlineFragment
                            else if (isFirstRequest || !requestingNewGifs
                                    || mObservableGifs.getValue() == null) {
                                Log.d(TAG, "postValue | First Request to the DB ");
                                mObservableGifs.postValue(gifs);
                                increaseSpecificOffset(gifs.size());

                            }
                            // 3) Other offset request to the DB
                            else {
                                Log.d(TAG, "postValue | Other offset request to the DB ");
                                List<Gif> tmp = mObservableGifs.getValue();
                                tmp.addAll(gifs);
                                mObservableGifs.postValue(tmp);
                                increaseOffset();

                            }
                            requestingNewGifs = false;
                            mLastTotalNumberOfGifs = mLocalRepository.getTotalNumberOfGifs();
                            Log.d(TAG, "awooo LastTotalNumberOfGifs = " + mLastTotalNumberOfGifs);
                            if (mOffset >= mLastTotalNumberOfGifs || mLastTotalNumberOfGifs
                                    <= LocalRepository.MAX_NUMBER_GIFS_RETURNED) {
                                mAllGifsRetrieved = true;
                            }
                        }
                    });
                }
            }
        };
    }

    /**
     * Remove from current list of Gifs (mObservableGifs) the last Gif removed by the user
     */
    private List<Gif> getListAfterGifRemoved() {
        String idRemoved = mLastGifRemoved.getServerId();
        List<Gif> currentList = mObservableGifs.getValue();
        Gif removedGif = null;
        for (Gif gif : currentList) {
            if (gif.getServerId().equals(idRemoved)) {
                removedGif = gif;
            }
        }
        if (removedGif != null) {
            currentList.remove(removedGif);
        }
        return currentList;
    }

    /**
     * This method helps to calculate the final list given the original one (the one being
     * displayed) and the one coming from the observer (on a new gif request to the DB or when a gif
     * is added/removed)
     */
//    private List<Gif> calculateNewList(List<Gif> gifs) {
//        DiffUtil.DiffResult result= DiffUtil.calculateDiff();
//
//    }
    public LiveData<List<Gif>> getGifs() {
        return mObservableGifs;
    }


    private void requestNewGifs() {
        Log.d(TAG, "RequestNewGifs | offset =" + mOffset);
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                /**
                 * if you only call addSource once, the observer won't be triggered in the
                 * following requests
                 */
                requestingNewGifs = true;
                LiveData<List<Gif>> gifsFromDB = mLocalRepository.getGifs(mOffset);
                //////////////////
                //TODO only for testing the behaviour
                //TODO if we go for it then the whole offset request in the DB is useless
                LiveData<List<Gif>> allGifsObserved = mLocalRepository.getAllGifs();

                //////////////////
                mObservableGifs.addSource(gifsFromDB, mObserver);
                isFirstRequest = false;
            }
        });
    }

    private void increaseSpecificOffset(int increment) {
        mOffset += increment;
    }

    private void increaseOffset() {
        mOffset += LocalRepository.MAX_NUMBER_GIFS_RETURNED;
    }

    private void decreaseOffsetBy1() {
        mOffset--;
    }

    public void onBottomReached() {
        if (mAllGifsRetrieved) {
            return;
        } else {
            Log.d(TAG, "onBottomReached | AllGifsRetrieved is false | RequestNewGifs...");
            requestNewGifs();
        }
    }

    public void onUnsetGifAsFavourite(final Gif gif) {
        mLastGifRemoved = gif;
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                if (mLocalRepository.removeGif(gif.getServerId()) > 0) {
                    Log.d(TAG, "gif removed");
                } else {
                    Log.d(TAG, "gif could NOT be removed");
                }
            }
        });

    }


    /**
     * This method will be called when the fragment is visible for the user
     */
    @Subscribe
    public void onVisibleEvent(final VisibleEvent event) {
        if (event.getPosition() == MainActivity.FAV_TAB) {
            //TODO or we enabled everything to start from scratch when swiping to offline fragment
            //TODO or we disabled everything
//            mAllGifsRetrieved = false;
//            isFirstRequest = true;
//            mOffset = 0;
            //TODO why?//////////
//            List<Gif> tmp= mGifs.getValue();
//            tmp.clear();
//            mGifs.postValue(tmp);
            //////////////////
            //TODO needed? u already did the very first fetch of data in the ViewModel creation
//            requestNewGifs();

        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        SignalManagerFactory.getSignalManager().unregister(this);

    }
}
