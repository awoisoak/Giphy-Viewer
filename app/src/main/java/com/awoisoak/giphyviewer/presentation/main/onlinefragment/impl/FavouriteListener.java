package com.awoisoak.giphyviewer.presentation.main.onlinefragment.impl;

import com.awoisoak.giphyviewer.data.Gif;

/**
 * Listener to inform whether a gif is saved in the DB
 */
public interface FavouriteListener {

    boolean isFavourite(Gif gif);
}
