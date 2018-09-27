package com.mindera.skeletoid.rxbindings.searchview;

import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;

import com.jakewharton.rxbinding2.InitialValueObservable;
import com.jakewharton.rxbinding2.widget.SearchViewQueryTextEvent;

import io.reactivex.functions.Consumer;

public class RxSearchView {

    public static InitialValueObservable<SearchViewQueryTextEvent> queryTextChangeEvents(
            @NonNull SearchView view) {
        return queryTextChangeEvents(view);
    }

    public static InitialValueObservable<CharSequence> queryTextChanges(@NonNull android.support.v7.widget.SearchView view) {
        return com.jakewharton.rxbinding2.support.v7.widget.RxSearchView.queryTextChanges(view);
    }

    public static Consumer<? super CharSequence> query(@NonNull final android.support.v7.widget.SearchView view,
            final boolean submit) {
        return com.jakewharton.rxbinding2.support.v7.widget.RxSearchView.query(view, submit);
    }

    public static InitialValueObservable<Boolean> focusChanges(@NonNull SearchView view) {
        return new SearchViewFocusChangeObservable(view);
    }

    public static InitialValueObservable<Boolean> focusChanges(@NonNull android.support.v7.widget.SearchView view) {
        return new com.mindera.skeletoid.rxbindings.searchview.support.SearchViewFocusChangeObservable(view);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
