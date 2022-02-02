package com.valet.mobilecart.network

import android.util.Log
import com.valet.mobilecart.interfaces.IApiCaller
import com.valet.mobilecart.interfaces.IResponseHandler
import com.valet.mobilecart.utils.EndPoints
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketException
import java.util.concurrent.TimeUnit

/**
 * RetrofitCommon class is called when the repository has to make API calls to fetch remote data
 */
object RetrofitCommon {

    val TAG = this.javaClass.simpleName
    private val retrofitBuilder: Retrofit
    private var job: CoroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        retrofitBuilder = Retrofit.Builder()
            .baseUrl(EndPoints.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client())
            .build()
    }

    /**
     * client() sets the header of the HTTP API calls.
     */
    private fun client(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor { message -> Log.i("API", message) }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectTimeout(15, TimeUnit.MINUTES) // connection timeout
            .readTimeout(300, TimeUnit.SECONDS)    // read timeout
            .writeTimeout(300, TimeUnit.SECONDS)   // write timeout
            .addInterceptor(NetworkAuthInterceptor())
            .build()
    }

    val callerInterface: IApiCaller = retrofitBuilder.create(IApiCaller::class.java)

    /**
     * makeCall is called when the response from the retrofit function is received.
     */
    fun <T : Any> makeCall(
        responseObservable: Response<T>,
        endpoint: String,
        apiHandlerInterface: IResponseHandler
    ) {
        job.launch {
            responseObservable.let {
                when {
                    it.isSuccessful -> {
                        when (it.code()) {
                            in 200..299 -> {
                                val data = it.body()
                                if (data != null)
                                    apiHandlerInterface.onSuccess(endpoint, data)
                                else
                                    apiHandlerInterface.onFailure(endpoint, "Null Response")
                            }
                            in 300..399 -> {
                                errorResponse(endpoint, "Unexpected Redirect", apiHandlerInterface)
                            }
                            in 400..499 -> {
                                errorResponse(endpoint, "Invalid Request", apiHandlerInterface)
                            }
                            in 500..599 -> {
                                errorResponse(
                                    endpoint,
                                    "Internal Server Error",
                                    apiHandlerInterface
                                )
                            }
                            else -> {
                                errorResponse(endpoint, "Something went wrong", apiHandlerInterface)
                            }
                        }
                    }
                    else -> {
                        onFailureResponse(apiHandlerInterface, endpoint, it.message())
                    }
                }
            }
        }
    }

    /**
     * onFailureResponse will be triggered when the call to the HTTP API breaks due to some reason.
     */
    private fun onFailureResponse(
        apiHandlerInterface: IResponseHandler,
        endpoint: String,
        message: String
    ) {
        apiHandlerInterface.onFailure(endpoint, message)
    }

    /**
     * onErrorResponse will be triggered when the HTTP fails to gets success response from the Web.
     */
    private fun errorResponse(
        endpoint: String,
        message: String,
        apiHandlerInterface: IResponseHandler
    ) {
        apiHandlerInterface.onFailure(endpoint, message)
    }
}