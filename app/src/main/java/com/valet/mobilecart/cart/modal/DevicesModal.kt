package com.valet.mobilecart.cart.modal

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * to parse/serialize the fields of DevicesModel
 */
class DevicesModal : Serializable {
    @SerializedName("Id")
    @Expose
    var Id: Int? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("Price")
    @Expose
    var price: Int? = null

    @SerializedName("Title")
    @Expose
    var title: String? = null

    @SerializedName("OS")
    @Expose
    var deviceOS: String? = null

    @SerializedName("Ratings")
    @Expose
    var ratings: Any? = null

    @SerializedName("Currency")
    @Expose
    var currency: String? = null

    @SerializedName("imageUrl")
    @Expose
    var imageUrl: String? = null

    @SerializedName("Design")
    @Expose
    var design: String? = null

    @SerializedName("Status")
    @Expose
    var status: String? = null

    @SerializedName("isFavorite")
    @Expose
    var isFavorite: Boolean? = null

    @SerializedName("Description")
    @Expose
    var description: String? = null
}