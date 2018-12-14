package com.bitbybitlabs.life.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitbybitlabs.life.Util

class SettingsActivity : AppCompatActivity() {

    private var parentClassName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupActionBar()
        // Display the fragment as the main content.
        supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, SettingsFragment())
                .commit()

        parentClassName = intent?.extras?.getString(Util.INTENT_BUNDLE_PARENT_NAME, MainActivity::class.java.name)
    }

    override fun getSupportParentActivityIntent(): Intent? {
//        return super.getSupportParentActivityIntent()
        var intent: Intent? = null
        if (parentClassName != null) {
            intent = Intent().setClassName(this, parentClassName)
        }
        return intent
    }

    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }
    }
}
