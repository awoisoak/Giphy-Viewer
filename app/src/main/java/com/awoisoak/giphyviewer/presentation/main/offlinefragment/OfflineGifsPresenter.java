package com.awoisoak.giphyviewer.presentation.main.offlinefragment;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.IFragmentPresenter;

public interface OfflineGifsPresenter extends IFragmentPresenter {

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    void onBottomReached();

    /**
     * Called when the user unset as favourite one of the gifs
     */
    void onUnsetGifAsFavourite(Gif gif);
}
