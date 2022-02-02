package com.valet.mobilecart.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.valet.mobilecart.R
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

/**
 * It contains function which are commonly used across the application
 */
object CommonUtils {
    private val TAG = this.javaClass.simpleName
    private val job: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var isInternetAvailableFlag = false

    /**
     * @param listener
     * It is called to check if internet is available
     */
    fun Context.isInternetConnected(
        listener: InternetConnect
    ): Boolean {
        var isConnected = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    isConnected = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && isInternetAvailableFlag -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) && isInternetAvailableFlag -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) && isInternetAvailableFlag -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                @Suppress("DEPRECATION")
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI && isInternetAvailableFlag) {
                        isConnected = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE && isInternetAvailableFlag) {
                        isConnected = true
                    }
                }
            }
        }
        if (isConnected) {
            listener.isConnect(true)
        } else {
            listener.isConnect(false)
        }
        return isConnected
    }

    @Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
    @OptIn(DelicateCoroutinesApi::class)
    suspend fun Context.callAsyncMethod(listener: InternetConnect) =
        withContext(job.coroutineContext) {
            pingAndCheck(this@callAsyncMethod, listener)
        }

    /**
     * @param context
     * @param listener
     * It is called to check if the internet can reach web or not
     */
    private suspend fun pingAndCheck(context: Context, listener: InternetConnect) =
        coroutineScope {
            var flag = false
            val one = async {
                try {
                    job.launch(Dispatchers.IO) {
                        try {
                            val sock = Socket()
                            val sockAddress: SocketAddress = InetSocketAddress("8.8.8.8", 53)
                            flag = try {
                                sock.connect(
                                    sockAddress,
                                    1000
                                ) // This will block no more than timeoutMs
                                sock.close()
                                true
                            } catch (e: Exception) {
                                Log.e(TAG, "SocketException1${e.message.toString()}")
                                false
                            }
                            isInternetAvailableFlag = flag
                            if (flag)
                                context.isInternetConnected(listener)
                            else
                                listener.isConnect(flag = false)
                        } catch (e: IOException) {
                            flag = false
                            listener.isConnect(flag = false)
                        } catch (e: InterruptedException) {
                            flag = false
                            listener.isConnect(flag = false)
                        }
                    }
                } catch (e: Exception) {
                    flag = false
                    listener.isConnect(flag = false)
                }
            }
            one.await()
        }

    interface InternetConnect {
        fun isConnect(flag: Boolean)
    }
}
