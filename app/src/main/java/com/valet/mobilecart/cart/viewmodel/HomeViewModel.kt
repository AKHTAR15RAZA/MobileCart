package com.valet.mobilecart.cart.viewmodel

import androidx.lifecycle.viewModelScope
import com.valet.mobilecart.base.BaseViewModel
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.launch

/**
 * HomeViewModel is the viewmodel which loads data from the repository and presents it to the view.
 */
class HomeViewModel(private val repository: HomeViewModelRepository) : BaseViewModel() {
    /**
     * deviceList() function calls the getDeviceList() method of repository to load the devices through an API
     */
    fun devicesList() {
        viewModelScope.launch {
            repository.getDevicesList(EndPoints.DATA, this@HomeViewModel, caller.sampleDevicesData())
        }
    }
}