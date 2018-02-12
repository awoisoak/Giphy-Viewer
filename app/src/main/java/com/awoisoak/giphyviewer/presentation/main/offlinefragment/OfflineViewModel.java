package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.local.impl.GifDao;
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
    public final LiveData<PagedList<Gif>> gifList;

    private LocalRepository mLocalRepository;



    public OfflineViewModel(LocalRepository localRepository) {
        SignalManagerFactory.getSignalManager().register(this);
        mLocalRepository = localRepository;
        mObservableGifs = new MediatorLiveData<>();
        gifList = new LivePagedListBuilder<>(mLocalRepository.getGifsWithPagedList(),
                LocalRepository.MAX_NUMBER_GIFS_RETURNED).build();
    }

    public LiveData<List<Gif>> getGifs() {
        return mObservableGifs;
    }

    public void onBottomReached() {
//        if (mAllGifsRetrieved) {
//            return;
//        } else {
//            Log.d(TAG, "onBottomReached | AllGifsRetrieved is false | RequestNewGifs...");
//        }
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
