package com.mindera.skeletoid.rxbindings.searchview;

import androidx.annotation.NonNull;
import android.widget.SearchView;

import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.appcompat.SearchViewQueryTextEvent;

public class RxSearchView {

    public static InitialValueObservable<SearchViewQueryTextEvent> queryTextChangeEvents(
            @NonNull SearchView view) {
        return queryTextChangeEvents(view);
    }

    /*public static InitialValueObservable<CharSequence> queryTextChanges(@NonNull androidx.appcompat.widget.SearchView view) {
        return com.jakewharton.rxbinding3.widget.RxSearchView.queryTextChanges(view);
    }

    public static Consumer<? super CharSequence> query(@NonNull final androidx.appcompat.widget.SearchView view,
            final boolean submit) {
        return com.jakewharton.rxbinding3.widget.RxSearchView.query(view, submit);
    }*/

    public static InitialValueObservable<Boolean> focusChanges(@NonNull SearchView view) {
        return new SearchViewFocusChangeObservable(view);
    }

    public static InitialValueObservable<Boolean> focusChanges(@NonNull androidx.appcompat.widget.SearchView view) {
        return new com.mindera.skeletoid.rxbindings.searchview.support.SearchViewFocusChangeObservable(view);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
