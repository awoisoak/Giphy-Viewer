package com.awoisoak.giphyviewer.presentation.main.offlinefragment;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.IFragmentPresenter;

public interface OfflineGifsPresenter extends IFragmentPresenter {

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    //TODO Probably not needed with the SimpleCursorAdapter
    void onBottomReached();

    /**
     * Called when the user set/unset as favourite one of the gifs
     */
    void onGifSetAsFavourite(Gif gif);
}
