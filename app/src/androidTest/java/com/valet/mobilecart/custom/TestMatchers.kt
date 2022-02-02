package com.valet.mobilecart.custom

import android.view.View
import org.hamcrest.Matcher

class TestMatchers {

    object EspressoTestsMatchers {
        fun withDrawable(resourceId: Int): Matcher<View> {
            return DrawableMatcher(resourceId)
        }

        fun noDrawable(): Matcher<View> {
            return DrawableMatcher(DrawableMatcher.EMPTY)
        }
    }
}