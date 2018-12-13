package com.bitbybitlabs.life.installed.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bitbybitlabs.life.BuildConfig
import com.bitbybitlabs.life.Util
import com.bitbybitlabs.life.base.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.play.core.splitinstall.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes


private const val TAG = "DynamicFeatures"
private const val weatherModuleName = "weather"
private const val financeModuleName = "finance"
private const val weatherPackageName = "com.bitbybitlabs.$weatherModuleName.ui"
private const val financePackageName = "com.bitbybitlabs.pkarels.$financeModuleName"

class MainActivity : AppCompatActivity(), DownloadFragment.OnDownloadFragmentInteractionListener {

    private lateinit var manager: SplitInstallManager

    private lateinit var progress: Group
    private lateinit var content: Group
    private lateinit var progressBar: ProgressBar
    private lateinit var progressText: TextView

    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        state.moduleNames().forEach { name ->
            // Handle changes in state.
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    //  In order to see this, the application has to be uploaded to the Play Store.
                    displayLoadingState(state, "Downloading $name")
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    /*
                      This may occur when attempting to download a sufficiently large module.

                      In order to see this, the application has to be uploaded to the Play Store.
                      Then features can be requested until the confirmation path is triggered.
                     */
                    startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    hideProgress()
                    onSuccessfulLoad(name)
                }

                SplitInstallSessionStatus.INSTALLING -> displayLoadingState(state, "Installing $name")
                SplitInstallSessionStatus.FAILED -> {
                    hideProgress()
                    toastAndLog("Error: ${state.errorCode()} for module ${state.moduleNames()}")
                }
            }
        }
    }

    private fun onSuccessfulLoad(moduleName: String) {
        when (moduleName) {
            weatherModuleName -> launchActivity("$weatherPackageName.WeatherActivity")
            financeModuleName -> launchActivity("$financePackageName.TransactionsActivity")
        }
    }

    private fun launchActivity(className: String) {
        Intent().setClassName(this, className)
                .also {
                    startActivity(it)
                }
    }

    /** Display a loading state to the user. */
    private fun displayLoadingState(state: SplitInstallSessionState, message: String) {
        displayProgress()

        progressBar.max = state.totalBytesToDownload().toInt()
        progressBar.progress = state.bytesDownloaded().toInt()

        updateProgressMessage(message)
    }

    private fun updateProgressMessage(message: String) {
        if (progress.visibility != View.VISIBLE) displayProgress()
        progressText.text = message
    }

    /** Display progress bar and text. */
    private fun displayProgress() {
        progress.visibility = View.VISIBLE
        content.visibility = View.GONE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
        content.visibility = View.VISIBLE
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                if (manager.installedModules.contains(weatherModuleName)) {
                    launchActivity("$weatherPackageName.WeatherActivity")
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, DownloadFragment.newInstance(weatherModuleName))
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_finance -> {
                if (manager.installedModules.contains(financeModuleName)) {
                    launchActivity("$financePackageName.TransactionsActivity")
                } else {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, DownloadFragment.newInstance(financeModuleName))
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun loadModule(name: String) {
        val request = SplitInstallRequest.newBuilder()
                .addModule(name)
                .build()

        manager.startInstall(request)
    }

    private fun toastAndLog(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        Log.d(TAG, text)
    }

    /** Set up all view variables. */
    private fun initializeViews() {
        content = findViewById(R.id.content)
        progress = findViewById(R.id.progress)
        progressBar = findViewById(R.id.progress_bar)
        progressText = findViewById(R.id.progress_text)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCenter.start(application, BuildConfig.APPCENTER_KEY, Analytics::class.java, Crashes::class.java)
        setContentView(R.layout.activity_main)
        initializeViews()

        manager = SplitInstallManagerFactory.create(this)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val zipPref = sharedPreferences.getString("pref_title_zip", "")
        if (zipPref == "") run { startActivity(Intent(this, SettingsActivity::class.java)) }
    }

    override fun onResume() {
        manager.registerListener(listener)
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val destinationIntent = Intent(this, SettingsActivity::class.java)
            Bundle().apply {
                putString(Util.INTENT_BUNDLE_PARENT_NAME, SettingsActivity::class.java.name)
                destinationIntent.putExtras(this)
            }
            startActivity(destinationIntent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        manager.unregisterListener(listener)
        super.onPause()
    }

    override fun onFragmentInteraction(moduleName: String?) {
        if (moduleName != null) loadModule(moduleName)
    }
}
