package com.valet.mobilecart.interfaces

import com.valet.mobilecart.cart.modal.ApiResponse
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET

interface IApiCaller {
    @GET(EndPoints.DATA)
    suspend fun sampleDevicesData(): Response<ApiResponse>
}