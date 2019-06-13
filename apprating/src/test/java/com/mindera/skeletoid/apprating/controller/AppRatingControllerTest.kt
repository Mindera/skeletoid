package com.mindera.skeletoid.apprating.controller

import android.content.Context
import com.mindera.skeletoid.apprating.callbacks.AppRatingDialogResponse
import com.mindera.skeletoid.apprating.store.AppRatingStore
import com.mindera.skeletoid.apprating.utils.DateUtils
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.Date
import kotlin.test.assertTrue

@RunWith(PowerMockRunner::class)
@PrepareForTest(AppRatingStore::class, AppRatingController::class)
class AppRatingControllerTest {

    private lateinit var context: Context
    private lateinit var controller: AppRatingController
    private lateinit var countsPerTimeInterval: Pair<Int, Long>
    private var promptTimeInterval: Long = 0L

    @Before
    fun setup() {
        context = Mockito.mock(Context::class.java)
        controller = AppRatingController()

        countsPerTimeInterval = Pair(3, 365L)
        promptTimeInterval = 30L
    }

    @Test
    fun promptDialogNoStoredValuesTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn("")
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(0)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn("")
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        val actual = controller.shouldPromptDialog(context)

        assertTrue { actual }
    }

    @Test
    fun promptDialogNoConditionsTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(0)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        val actual = controller.shouldPromptDialog(context)

        assertTrue { actual }
    }

    @Test
    fun promptDialogAlreadyRated() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(0)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(true)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        val actual = controller.shouldPromptDialog(context)

        assertTrue { !actual }
    }

    @Test
    fun promptDialogTimeBetweenNotPassesTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn("2018-05-05")
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(0)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
        val actual = controller.shouldPromptDialog(context)

        assertTrue { !actual }
    }

    @Test
    fun promptDialogCountExceededTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn(DateUtils.formatDate(Date()))
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(4)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn("2018-05-05")
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
        val actual = controller.shouldPromptDialog(context)

        assertTrue { !actual }
    }

    @Test
    fun promptDialogCountRangeExceededTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.initialPromptDate).thenReturn("2018-05-05")
        PowerMockito.`when`(mockedStore.promptedCount).thenReturn(4)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)
        PowerMockito.`when`(mockedStore.lastTimePrompted).thenReturn("2018-05-05")
        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        controller.setupConditions(countsPerTimeInterval, promptTimeInterval)
        val actual = controller.shouldPromptDialog(context)

        assertTrue { actual }
    }

    @Test
    fun handleRateDialogResultsTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)

        //This way we know that the setter was called
        PowerMockito.doAnswer {
                PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(true)
            }.`when`(mockedStore).alreadyRated = Mockito.anyBoolean()

        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        controller.handleDialogResponse(context, AppRatingDialogResponse.RATE_NOW)
        assertTrue { mockedStore.alreadyRated }

        controller.handleDialogResponse(context, AppRatingDialogResponse.NEVER_RATE)
        assertTrue { mockedStore.alreadyRated }
    }

    @Test
    fun handleNeverRateDialogResultsTest() {
        val mockedStore = PowerMockito.mock(AppRatingStore::class.java)
        PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(false)

        //This way we know that the setter was called
        PowerMockito.doAnswer {
                PowerMockito.`when`(mockedStore.alreadyRated).thenReturn(true)
            }.`when`(mockedStore).alreadyRated = Mockito.anyBoolean()

        PowerMockito.whenNew(AppRatingStore::class.java).withArguments(context).thenReturn(mockedStore)

        controller.handleDialogResponse(context, AppRatingDialogResponse.NEVER_RATE)
        assertTrue { mockedStore.alreadyRated }
    }
}