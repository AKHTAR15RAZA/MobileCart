package com.valet.mobilecart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.lifecycleScope
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.valet.mobilecart.base.BaseActivity
import com.valet.mobilecart.cart.view.HomeActivity
import com.valet.mobilecart.databinding.ActivityMainBinding
import kotlinx.coroutines.delay

/**
 * First page when the app loads, it shows the splash screen and stays for 1500ms.
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private val splashTime: Long = 1500 //Time to wait before going to next page

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        hideBar()
        lifecycleScope.launchWhenResumed {
            delay(splashTime)
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK //to clear the stack and load new activity as a root page or first item in the stack
            startActivity(intent)
        }
    }

}