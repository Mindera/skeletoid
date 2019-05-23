package com.mindera.skeletoid.analytics.appenders;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class FBAppenderUnitTest {

    private static final String mPackageName = "my.package.name";

    private android.content.Context mContext;

    @org.junit.Before
    public void setUp() {
        mContext = mock(android.content.Context.class);
    }

    @org.junit.Test
    public void testFBNotNull() {
        FBAppender fb = new FBAppender();

        fb.disableAppender();

        assertNotNull(fb);
    }
}
