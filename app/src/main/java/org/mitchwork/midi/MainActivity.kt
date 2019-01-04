package org.mitchwork.midi

import android.content.Context
import android.content.pm.PackageManager
import android.media.midi.MidiDeviceInfo.PROPERTY_NAME
import android.media.midi.MidiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.mitchwork.midi.databinding.ActivityMainBinding
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModel
import org.mitchwork.midi.viewmodels.AvailableMidiDevicesViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private var isBluetoothSupported: Boolean = false
    private var isMidiSupported: Boolean = false

    private lateinit var activityViewModel: AvailableMidiDevicesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBluetoothSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
        isMidiSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_MIDI)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        drawerLayout = binding.drawerLayout

        navController = Navigation.findNavController(this, R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        navigationView = binding.navigationView

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        // Set up navigation menu
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_settings -> {
                    navController.navigate(R.id.action_global_settingsFragment)
                }
                R.id.nav_midi_devices -> {
                    navController.navigate(R.id.action_global_availableMidiDevices)
                }
                R.id.nav_bluetooth -> {
                    navController.navigate(R.id.action_global_scan_bluetooth_devices)
                }
                else -> {
                    Log.e("MIDI", "selected menu item: ${menuItem.itemId}")
                }
            }
            true
        }
        navigationView.menu.findItem(R.id.nav_bluetooth).isVisible = isBluetoothSupported
        navigationView.menu.findItem(R.id.nav_midi_devices).isVisible = isMidiSupported

        val midiManager: MidiManager = getSystemService(Context.MIDI_SERVICE) as MidiManager
        activityViewModel = ViewModelProviders.of(
            this, AvailableMidiDevicesViewModelFactory(midiManager)
        ).get(AvailableMidiDevicesViewModel::class.java)

        activityViewModel.midiDevice.observe(this, Observer {
            val subHeader = navigationView.findViewById<TextView>(R.id.nav_header_subtitle)
            if (it == null) {
                subHeader?.visibility = View.GONE
                subHeader?.text = ""
            } else {
                subHeader?.visibility = View.VISIBLE
                subHeader?.text =
                        getString(
                            R.string.navigation_drawer_header_subtitle,
                            it.info.properties.getString(PROPERTY_NAME)
                        )
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

}
