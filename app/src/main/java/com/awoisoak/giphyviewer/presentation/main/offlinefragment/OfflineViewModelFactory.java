package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.awoisoak.giphyviewer.data.local.LocalRepository;

/**
 * Created by awo on 1/24/18.
 */

public class OfflineViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    LocalRepository mLocalRepository;

    public OfflineViewModelFactory(LocalRepository localRepository){
        mLocalRepository = localRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new OfflineViewModel(mLocalRepository);
    }
}
