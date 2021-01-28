package com.mindera.skeletoid.rxbindings.extensions.views.searchview

import androidx.appcompat.widget.SearchView
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito

class RxSearchViewUnitTest {

    @Test
    fun `test RxSearchView`() {
        val searchView = Mockito.mock(SearchView::class.java)
        Assert.assertNotNull(RxSearchView.focusChanges(searchView))
    }
}