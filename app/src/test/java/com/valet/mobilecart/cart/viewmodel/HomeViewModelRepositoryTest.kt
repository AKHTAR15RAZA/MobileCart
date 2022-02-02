package com.valet.mobilecart.cart.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.valet.mobilecart.base.BaseViewModel
import com.valet.mobilecart.network.RetrofitCommon
import com.valet.mobilecart.util.MainCoroutineRule
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.valet.mobilecart.interfaces.IApiCaller
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModelRepositoryTest : BaseViewModel() {
    private lateinit var homeViewModelRepository: HomeViewModelRepository
    private val retrofitCommon = RetrofitCommon
    val httpClient = OkHttpClient.Builder()
    @Before
    fun setUp() {
        homeViewModelRepository = HomeViewModelRepository(retrofitCommon = RetrofitCommon)
        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + EndPoints.API_TOKEN)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        })
    }

    @Test
    fun `returns list of devices when API's are called`() = runBlocking {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        val caller = retrofitBuilder.create(IApiCaller::class.java)
        val response = caller.sampleDevicesData()
        assertThat(response.code()).isEqualTo(200)
        assertThat(response.body()!!.devices!!.size).isAtLeast(1)
    }



}