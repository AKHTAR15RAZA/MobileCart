package com.valet.mobilecart.cart.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.idling.CountingIdlingResource
import com.valet.mobilecart.R
import com.valet.mobilecart.base.BaseActivity
import com.valet.mobilecart.cart.modal.ApiResponse
import com.valet.mobilecart.cart.modal.DevicesModal
import com.valet.mobilecart.cart.viewmodel.HomeViewModel
import com.valet.mobilecart.cart.viewmodel.HomeViewModelFactory
import com.valet.mobilecart.databinding.ActivityHomeBinding
import com.valet.mobilecart.utils.CommonUtils
import com.valet.mobilecart.utils.CommonUtils.callAsyncMethod
import com.valet.mobilecart.utils.Constants.hide
import com.valet.mobilecart.utils.Constants.show
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.launch
import org.kodein.di.generic.instance


/**
 * First page or home page of the application, it loads the list of devices, also has an option of search,
 * To filter out devices on the basis of keywords
 */
class HomeActivity : BaseActivity(), CommonUtils.InternetConnect {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var devicesAdapter: DevicesAdapter
    private lateinit var viewModel: HomeViewModel
    private val factory: HomeViewModelFactory by instance()
    private val devicesList: ArrayList<DevicesModal> = ArrayList()
    var homeMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incToolbar)
        idlingResource = CountingIdlingResource("loadingData")

        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
        lifecycleScope.launchWhenStarted {
            showLoading()
        }

        lifecycleScope.launch {
            idlingResource.increment()
            this@HomeActivity.callAsyncMethod(this@HomeActivity)
        }

        devicesAdapter = DevicesAdapter(this@HomeActivity)
        binding.apply {
            rcvDevicesList.apply {
                this.layoutManager =
                    LinearLayoutManager(this@HomeActivity, LinearLayoutManager.VERTICAL, false)
                this.adapter = devicesAdapter
            }

            tryNow.setOnClickListener {
                retryLoading()
            }
        }
        setObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        homeMenu = menu
        val search = menu.findItem(R.id.appSearchBar)
        val searchView = search.actionView as SearchView
        searchView.queryHint = "Search Devices"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.toString().trim().isEmpty() or newText.toString().trim().isBlank()) {
                    devicesAdapter.updateData(devicesList)
                } else {
                    devicesAdapter.filter.filter(newText.toString())
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    @Override
    override fun onStart() {
        super.onStart()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObservers() {
        viewModel.loadingManager.observe(this) {
            if (it.second.size != 0) {
                showLoading()
            } else {
                dismissLoading()
            }
        }
        viewModel.response.observe(this) {
            binding.rcvDevicesList.show()
            binding.noInternetLayout.hide()
            idlingResource.decrement()
            if (it.containsKey(EndPoints.DATA)) {
                val response = it[EndPoints.DATA] as ApiResponse
                if (response.devices != null) {
                    response.devices?.let {
                        if (it.isNotEmpty()) {
                            devicesList.clear()
                            devicesList.addAll(it)
                            devicesAdapter.updateData(devicesList)
                            homeMenu?.findItem(R.id.appSearchBar)?.setVisible(true)
                        }
                    }
                }
            }
        }
        viewModel.responseError.observe(this) {
            idlingResource.decrement()
            if (it.containsKey(EndPoints.DATA)) {
                binding.rcvDevicesList.hide()
                binding.noInternetLayout.show()
                homeMenu?.findItem(R.id.appSearchBar)?.setVisible(false)
                it.remove(EndPoints.DATA)
            }
        }
    }

    /**
     * check if internet is available
     */
    override fun isConnect(flag: Boolean) {
        if (!flag) {
            Handler(Looper.getMainLooper()).post {
                dismissLoading()
                binding.rcvDevicesList.hide()
                binding.noInternetLayout.show()
                homeMenu?.findItem(R.id.appSearchBar)?.setVisible(false)
            }
        } else {
            Handler(Looper.getMainLooper()).post {
                homeMenu?.findItem(R.id.appSearchBar)?.setVisible(true)
            }
            viewModel.devicesList()
        }
    }

    /**
     * when the app is loaded with no internet and it gets back internet try now will load all the devices
     */
    fun retryLoading() {
        binding.rcvDevicesList.hide()
        binding.noInternetLayout.hide()
        lifecycleScope.launchWhenStarted {
            showLoading()
        }
        lifecycleScope.launch {
            idlingResource.increment()
            this@HomeActivity.callAsyncMethod(this@HomeActivity)
        }
    }

    /**
     * override back button funtionality
     */
    var doubleBackToExitPressedOnce: Boolean = false;
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, R.string.double_back_button, Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @VisibleForTesting
    @NonNull
    private lateinit var idlingResource: CountingIdlingResource
    fun getIdlingResource(): IdlingResource? {
        return idlingResource
    }
}