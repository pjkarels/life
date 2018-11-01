package com.bitbybitlabs.life

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.bitbybitlabs.life", appContext.packageName)
    }

    @Test
    fun zipLocationFound() {
        val appContext = InstrumentationRegistry.getTargetContext()
        ZipCodeService.getLatLong(appContext, "28652")
    }

    @Test
    fun zipLocationNotFound() {
        val appContext = InstrumentationRegistry.getTargetContext()
        ZipCodeService.getLatLong(appContext, "12345")
    }
}
