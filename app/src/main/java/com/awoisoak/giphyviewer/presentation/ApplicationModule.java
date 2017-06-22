package com.awoisoak.giphyviewer.presentation;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Application Dagger Module that will provide the Context to the rest of the application
 */
@Module
public class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context mContext) {
        this.mContext = mContext;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
