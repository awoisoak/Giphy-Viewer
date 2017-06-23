package com.awoisoak.giphyviewer.presentation.main;

/**
 * Event to be sent by the Activity when a Fragment is visible to the user
 */

public class VisibleEvent {
    int position;


    public VisibleEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}
