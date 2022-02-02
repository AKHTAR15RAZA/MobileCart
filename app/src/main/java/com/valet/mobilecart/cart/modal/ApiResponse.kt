package com.valet.mobilecart.cart.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * to parse/serialize the devices while loading the data from the API response
 */
class ApiResponse:Serializable {
    @SerializedName("devices")
    @Expose
    var devices: List<DevicesModal>? = null
}