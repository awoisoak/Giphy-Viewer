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

public class OfflineViewModel extends ViewModel {
    private static final Object LOCK = new Object();
    public static String TAG = "awoooo" + OfflineViewModel.class.getSimpleName();
    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    public final LiveData<PagedList<Gif>> gifList;

    private LocalRepository mLocalRepository;



    public OfflineViewModel(LocalRepository localRepository) {
        Log.d(TAG,"awoooo OfflineViewModel Constructor called!");
        SignalManagerFactory.getSignalManager().register(this);
        mLocalRepository = localRepository;
        gifList = new LivePagedListBuilder<>(mLocalRepository.getGifsWithPagedList(),
                LocalRepository.MAX_NUMBER_GIFS_RETURNED).build();
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
            //we don't need to do anything here on this branch
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        SignalManagerFactory.getSignalManager().unregister(this);

    }
}
