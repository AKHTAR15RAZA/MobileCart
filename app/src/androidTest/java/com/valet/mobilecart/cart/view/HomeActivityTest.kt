package com.valet.mobilecart.cart.view

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.valet.mobilecart.R
import org.junit.After
import org.junit.Before
import org.junit.Test


class HomeActivityTest {
    private var mIdlingResource: IdlingResource? = null
    private lateinit var scenario : ActivityScenario<HomeActivity>

    @Before
    fun setUp(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.RESUMED)

        scenario.onActivity {
            mIdlingResource = it.getIdlingResource()
            IdlingRegistry.getInstance().register(mIdlingResource);
        }
    }

    @Test
    fun recyclerViewIsLoaded(){
        onView(ViewMatchers.withId(R.id.rcv_devices_list)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun recyclerViewClick(){
        onView(ViewMatchers.withText("Samsung Galaxy S7")).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.txtOSDevice)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withId(R.id.txtOSDevice)).check(ViewAssertions.matches(ViewMatchers.withText("Android 6")))
    }

    @After
    fun unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource)
        }
    }
}