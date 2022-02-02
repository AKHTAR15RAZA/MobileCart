package com.valet.mobilecart.cart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.valet.mobilecart.network.RetrofitCommon

class HomeViewModelFactory(private val retrofitInstance: RetrofitCommon) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass == HomeViewModel::class.java) {
            HomeViewModel(HomeViewModelRepository(retrofitInstance)) as T
        } else null!!
    }
}