package com.codefal.mystoryapp

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.codefal.mystoryapp.databinding.ActivityMainBinding
import com.codefal.mystoryapp.viewmodel.PrefViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val prefModel : PrefViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        navController.setGraph(R.navigation.nav)

        prefModel.getStatus().observe(this) {
            if (it) {
                navController.navigate(R.id.storyFragment)
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.change_lang -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.logout -> {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.logout))
                    .setMessage(resources.getString(R.string.are_you_sure))
                    .setCancelable(false)
                    .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                        // Respond to negative button press
                        dialog.cancel()
                    }
                    .setPositiveButton(resources.getString(R.string.logout)) { _, _ ->
                        // Respond to positive button press
                        prefModel.logout()
                        navController.navigate(R.id.loginFragment)
                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}