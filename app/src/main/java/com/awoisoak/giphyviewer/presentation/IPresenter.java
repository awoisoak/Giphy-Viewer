package com.awoisoak.giphyviewer.presentation;


/**
 * Interface to be implemented by all Presenters
 *
 * - Signals registration should be done in onCreate/onDestroy or onStart/onStop depending on the requirements
 * - View reference shall be set to null on onDestroy
 *
 */
public interface IPresenter {

    void onCreate();

    void onDestroy();

    void onStart();

    void onStop();

    void onResume();

    void onPause();

}
