package com.valet.mobilecart.network

import android.util.Log
import com.valet.mobilecart.utils.Constants.hide
import com.valet.mobilecart.utils.EndPoints
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/**
 * NetworkAuthInterceptor is called to add interceptors in the API's
 */
class NetworkAuthInterceptor : Interceptor {
    private val TAG = this.javaClass.simpleName
    override fun intercept(chain: Interceptor.Chain): Response {
        val loRequestURL: String = chain.request().url.toString()
        try {
            var mainResponse = chain.proceed(chain.request())
            val mainRequest: Request = chain.request()
            when (mainResponse.code) {
                401, 403 -> {
                    mainResponse.close()
                    val builder = mainRequest.newBuilder()
                        .header("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + EndPoints.API_TOKEN)
                        .method(chain.request().method, chain.request().body)
                    mainResponse = chain.proceed(builder.build())
                }
            }
            return mainResponse
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            val loMockRequest: Request = Request.Builder()
                .url(loRequestURL)
                .build()
            val loBuilder: Response.Builder = Response.Builder()
            loBuilder.request(loMockRequest).protocol(Protocol.HTTP_2).protocol(Protocol.HTTP_1_1)
                .code(1).message("Exception")
                .body("{}".toResponseBody("application/json; charset=utf-8".toMediaType()))
            return loBuilder.build()
        }
    }
}

