package com.valet.mobilecart

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ServiceTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.valet.mobilecart.base.BaseActivity
import com.valet.mobilecart.cart.view.HomeActivity
import com.valet.mobilecart.custom.TestMatchers.EspressoTestsMatchers.withDrawable
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    private lateinit var scenario : ActivityScenario<MainActivity>

    @Before
    fun setUp(){
        scenario = launchActivity()
        scenario.moveToState(Lifecycle.State.STARTED)
        scenario.moveToState(Lifecycle.State.RESUMED)
    }


    @Test
    fun splashImageIsVisible(){
        onView(ViewMatchers.withId(R.id.imageview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun splashImageIsShown(){
        onView(ViewMatchers.withId(R.id.imageview)).check(ViewAssertions.matches(withDrawable(R.drawable.app_splash)))
    }

}