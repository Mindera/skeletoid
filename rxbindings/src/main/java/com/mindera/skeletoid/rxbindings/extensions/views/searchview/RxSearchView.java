package com.mindera.skeletoid.rxbindings.extensions.views.searchview;

import androidx.annotation.NonNull;

import com.jakewharton.rxbinding3.InitialValueObservable;
import com.jakewharton.rxbinding3.appcompat.SearchViewQueryTextEvent;
import com.mindera.skeletoid.rxbindings.extensions.views.searchview.support.SearchViewFocusChangeObservable;
import androidx.appcompat.widget.SearchView;

public class RxSearchView {

    public static InitialValueObservable<SearchViewQueryTextEvent> queryTextChangeEvents(
            @NonNull SearchView view) {
        return queryTextChangeEvents(view);
    }

    public static InitialValueObservable<Boolean> focusChanges(@NonNull SearchView view) {
        return new SearchViewFocusChangeObservable(view);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
