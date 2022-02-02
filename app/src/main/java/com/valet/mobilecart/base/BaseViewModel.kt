package com.valet.mobilecart.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.valet.mobilecart.interfaces.IResponseHandler
import com.valet.mobilecart.network.RetrofitCommon
import java.util.*
import kotlin.collections.HashMap

/**
 *  It is the parent view model of HomeViewModel which implements the functions of ViewModel
 *  as well as IResponseHandler such as onSuccess,onFailure, showLoading, dismissLoading
 * */
open class BaseViewModel : ViewModel(), IResponseHandler {
    val response = MutableLiveData<HashMap<String, Any>>()
    val responseError = MutableLiveData<HashMap<String?, Any?>>()
    private val loadingQueue = LinkedList<String>()
    val loadingManager = MutableLiveData<Pair<String, Queue<String>>>()
    private val retrofit = RetrofitCommon
    val caller = retrofit.callerInterface

    /**
     * @param endpoint
     * @param response
     * called when the HTTP APO's are called successfully
     */
    override fun onSuccess(endpoint: String, response: Any) {
        this.response.postValue(hashMapOf(Pair(endpoint, response)))
        dismissLoading(endpoint)
    }

    /**
     * @param endpoint
     * @param reason
     * called when there is failure in reaching the HTTP API
     */
    override fun onFailure(endpoint: String?, reason: String?) {
        this.responseError.postValue(hashMapOf(Pair(endpoint, reason)))
        dismissLoading(endpoint!!)
    }

    /**
     * @param endpoint
     * called to present/show the loading dialog
     */
    override fun showLoading(endpoint: String) {
        loadingQueue.add(endpoint)
        loadingManager.postValue(Pair(first = endpoint, second = loadingQueue))
    }

    /**
     * @param endpoint
     * called to dismiss the loading dialog
     */
    override fun dismissLoading(endpoint: String) {
        loadingQueue.poll()
        loadingManager.postValue(Pair(first = endpoint, second = loadingQueue))
    }
}