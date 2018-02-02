package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;

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

public class OfflineViewModel extends ViewModel {
    public static String TAG = OfflineViewModel.class.getSimpleName();

    private MutableLiveData<List<Gif>> mGifs = new MutableLiveData<>();
    private boolean isFirstRequest = true;
    private boolean mAllGifsRetrieved;
    private int mOffset;
    private LocalRepository mLocalRepository;

    public OfflineViewModel(LocalRepository localRepository) {
        mLocalRepository = localRepository;
        SignalManagerFactory.getSignalManager().register(this);
        observeDatabase();
    }


    public LiveData<List<Gif>> getGifs() {
        return mGifs;
    }


    private void observeDatabase() {
        // As long as the repository exists, observe the network LiveData.
        mLocalRepository.getAllGifs().observeForever(new Observer<List<Gif>>() {
            @Override
            public void onChanged(@Nullable final List<Gif> gifs) {
                //for some reason this method can be called in UI thread
                System.out.println("awooooo | OfflineViewModel | database changed | size = " + gifs.size());
                ThreadPool.run(new Runnable() {
                    @Override
                    public void run() {
                        mGifs.postValue(gifs);
                        increaseOffset();
                        if (mOffset >= mLocalRepository.getTotalNumberOfGifs()) {
                            mAllGifsRetrieved = true;
                        }
                    }
                });

            }
        });


    }

    /**
     * Retrieve gifs from the DB and update the UI exposed data (mGifs)
     */
    //TODO we should change the structure, this is being called from different methods as it was
    // supposed to be called to request new data with a given offset
    // here we should call a fectch method data and increase the offset, but the observation
    // itself should be in onCreate so it's called just once
    //TODO besides that the gifsFromDB is a local variable so it must be stopping to observer
    // every single time it leaves the method
    //TODO actually it looks like is not needed
    private void requestNewGifs() {
        ThreadPool.run(new Runnable() {
            @Override
            public void run() {
                LiveData<List<Gif>> gifsFromDB = mLocalRepository.getGifs(mOffset);
                // As long as the repository exists, observe the network LiveData.
                gifsFromDB.observeForever(new Observer<List<Gif>>() {
                    @Override
                    public void onChanged(@Nullable final List<Gif> gifs) {
                        //for some reason this method can be called in UI thread
                        System.out.println("awoooo database changed | size = " + gifs.size());
                        ThreadPool.run(new Runnable() {
                            @Override
                            public void run() {
                                mGifs.postValue(gifs);
                                increaseOffset();
                                if (mOffset >= mLocalRepository.getTotalNumberOfGifs()) {
                                    mAllGifsRetrieved = true;
                                }
                            }
                        });

                    }
                });

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
                if (mLocalRepository.removeGif(gif.getId()) > 0) {
                    System.out.println(
                            "awooooooooo | OffLineViewModel | onUnsetGifasFavourite | removed");
//                    //TODO this should be necessary if livedata detect the delete queries too
//                    List<Gif> tmp = mLocalRepository.getAllGifs().getValue();
//                    mLocalRepository.removeAllGifs();
//                    mLocalRepository.addGifs((Gif[]) tmp.toArray());
//                    mGifs.postValue(tmp);
                } else {
                    System.out.println(
                            "awooooooooo | OffLineViewModel | onUnsetGifasFavourite | not removed");

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
