package com.awoisoak.giphyviewer.presentation.main.offlinefragment;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.IPresenter;

public interface OfflineGifsPresenter extends IPresenter {

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
