package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
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
    public static String TAG = OfflineViewModel.class.getSimpleName();
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Gif>> mObservableGifs;
    private Observer mObserver;
    private boolean isFirstRequest = true;
    private boolean mAllGifsRetrieved;
    private int mOffset;
    private LocalRepository mLocalRepository;


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
                ThreadPool.run(new Runnable() {
                    @Override
                    public void run() {
                        //TODO use DIffUtil here? what about if its observing an offset request?
                        //TODO in that case we should postValue only the specific gifs retrieved
//                        List<Gif> newList = calculateNewList(gifs);

                        //////
                        //TODO this works on new requests but not when removing data as we are
                        // adding the removed element again
                        //TODO we should check which is the removed value and not post it in the
                        // observable so the adapter can later update the list accordingly
                        if (mLocalRepository.getTotalNumberOfGifs() < lastTotalNumberOfGifs){
                            //TODO remove the removed element from mObservable and post the rest
                            checkElementRemoved(gifs);
                        }

                        ////
                        if (isFirstRequest || mObservableGifs.getValue() == null) {
                            mObservableGifs.postValue(gifs);
                        } else {
                            List<Gif> tmp = mObservableGifs.getValue();
                            tmp.addAll(gifs);
                            mObservableGifs.postValue(tmp);
                        }
                        /////
                        increaseOffset();
                        int totalNumberOfGifs = mLocalRepository.getTotalNumberOfGifs();
                        Log.d(TAG, "awooo totalNumberOfGifs = " + totalNumberOfGifs);
                        if (mOffset >= totalNumberOfGifs) {
                            mAllGifsRetrieved = true;
                        }
                    }
                });
            }
        };
    }

    private void checkElementRemoved(List<Gif> gifs) {
//        DiffUtil.DiffResult result= DiffUtil.calculateDiff();
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


//    private void retrieveAllGifs() {
//        LiveData<List<Gif>> allGifsFromDB = mLocalRepository.getAllGifs();
//        mObservableGifs.addSource(allGifsFromDB, mObserver);
//    }

    private void requestNewGifs() {
        //TODO we are using the local_id when using the offset for new requests
        //TODO what about we remove them from the DB?, the local_id will dissapear so the offset
        //TODO  won't make sense anymore
        Log.d(TAG, "Request New Gifs | offset =" + mOffset);
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                LiveData<List<Gif>> gifsFromDB = mLocalRepository.getGifs(mOffset);
                mObservableGifs.addSource(gifsFromDB, mObserver);
                isFirstRequest = false;
            }
        });
    }

    public void increaseOffset() {
        mOffset += LocalRepository.MAX_NUMBER_GIFS_RETURNED;
    }

    public void onBottomReached() {
        if (mAllGifsRetrieved) {
            return;
        } else {
            requestNewGifs();
        }
    }

    public void onUnsetGifAsFavourite(final Gif gif) {
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
            mAllGifsRetrieved = false;
            isFirstRequest = true;
            mOffset = 0;
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
