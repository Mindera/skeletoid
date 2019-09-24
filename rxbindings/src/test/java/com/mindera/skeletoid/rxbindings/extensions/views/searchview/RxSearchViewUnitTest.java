package com.mindera.skeletoid.rxbindings.extensions.views.searchview;

import android.content.Context;
import androidx.appcompat.widget.SearchView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RxSearchViewUnitTest {

    private String mPackageName = "my.package.name";

    private Context mContext;

    @Before
    public void setUp() {
        mContext = mock(Context.class);
    }


    @Test
    public void testDisableAppender() {
        SearchView sv = mock(SearchView.class);
        assertNotNull(RxSearchView.focusChanges(sv));
    }

}
