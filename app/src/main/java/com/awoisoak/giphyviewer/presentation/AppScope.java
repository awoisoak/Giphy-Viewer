package com.awoisoak.giphyviewer.presentation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scope for the App lifecycle
 */
@Documented
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface AppScope {
}
