package org.mitchwork.midi

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.mitchwork.midi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    private var isBluetoothSupported: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isBluetoothSupported = packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)

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
                    val direction = MidiDeviceListFragmentDirections.actionGlobalSettingsFragment()
                    navController.navigate(direction)
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
