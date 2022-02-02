package com.valet.mobilecart.cart.viewmodel

import com.valet.mobilecart.cart.modal.ApiResponse
import com.valet.mobilecart.interfaces.IResponseHandler
import com.valet.mobilecart.network.RetrofitCommon
import com.valet.mobilecart.utils.CommonUtils
import retrofit2.Response

/**
 * HomeViewModelRepository is the repository of the home page which loads the device information through retrofit
 */
class HomeViewModelRepository(private val retrofitCommon: RetrofitCommon) {
    fun getDevicesList(
        endpoint: String,
        apiHandler: IResponseHandler,
        devicesResponse: Response<ApiResponse>
    ) {
        retrofitCommon.makeCall(
            devicesResponse,
            endpoint = endpoint,
            apiHandlerInterface = apiHandler
        )
    }
}