package com.awoisoak.giphyviewer.presentation.main.onlinefragment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.awoisoak.giphyviewer.data.local.LocalRepository;
import com.awoisoak.giphyviewer.data.remote.GiphyApi;

/**
 * Created by awo on 1/24/18.
 */

public class OnlineViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    LocalRepository mLocalRepository;
    GiphyApi mRemoteRepository;

    public OnlineViewModelFactory(LocalRepository localRepository,
            GiphyApi remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OnlineViewModel(mLocalRepository, mRemoteRepository);
    }
}
