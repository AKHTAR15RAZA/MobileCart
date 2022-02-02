package com.valet.mobilecart.cart.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valet.mobilecart.cart.modal.ApiResponse
import com.valet.mobilecart.interfaces.IResponseHandler
import com.valet.mobilecart.network.RetrofitCommon
import com.valet.mobilecart.util.MainCoroutineRule
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import retrofit2.Response


class HomeViewModelTest {
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp(){
        homeViewModel = HomeViewModel(HomeViewModelRepository(retrofitCommon = RetrofitCommon))
    }

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Test
    fun `should return device list when network is available`() = runBlocking {
        val devices = homeViewModel.devicesList()
        assertThat(homeViewModel.response).isNotNull()
        assertThat(devices).isNotNull()
    }
}