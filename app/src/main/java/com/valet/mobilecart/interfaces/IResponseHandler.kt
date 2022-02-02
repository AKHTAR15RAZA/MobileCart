package com.valet.mobilecart.interfaces

interface IResponseHandler {
    fun onSuccess(endpoint: String, response: Any)
    fun onFailure(endpoint: String?, reason: String?)
    fun dismissLoading(endpoint: String)
    fun showLoading(endpoint: String)
}