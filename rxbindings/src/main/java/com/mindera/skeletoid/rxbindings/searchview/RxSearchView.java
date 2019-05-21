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

    public static InitialValueObservable<Boolean> focusChanges(@NonNull androidx.appcompat.widget.SearchView view) {
        return new com.mindera.skeletoid.rxbindings.searchview.support.SearchViewFocusChangeObservable(view);
    }

    private RxSearchView() {
        throw new AssertionError("No instances.");
    }
}
