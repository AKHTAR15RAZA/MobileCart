package com.valet.mobilecart.cart.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.valet.mobilecart.R
import com.valet.mobilecart.cart.modal.DevicesModal
import com.valet.mobilecart.databinding.ListDeviceItemBinding
import com.valet.mobilecart.utils.Constants
import java.util.*


/**
 * DevicesAdapter is the BaseAdapter for the RecyclerView presented on the home screen
 * It inflates a view of list_device_item.xml for the recyclerview
 */
class DevicesAdapter(val context: Context) :
    RecyclerView.Adapter<DevicesAdapter.ViewHolder>(), Filterable {

    private var devicesList: ArrayList<DevicesModal> = ArrayList()
    private var devicesListFiltered: ArrayList<DevicesModal> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DevicesAdapter.ViewHolder {
        return ViewHolder(
            ListDeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: DevicesAdapter.ViewHolder, position: Int) {
        val item = devicesListFiltered[position]
        with(holder) {
            with(binding) {
                val status = "Status: ${item.status}"
                val rating = "Rating: ${item.ratings}/5"
                val price = "Price: ${item.currency} ${item.price}"
                deviceName.text = item.title
                deviceStatus.text = status
                deviceRating.text = rating
                devicePrice.text = price
                val myOptions = RequestOptions()
                    .fitCenter() // or centerCrop
                    .override(200, 200)
                Glide.with(context).load(item.imageUrl).apply(myOptions).placeholder(R.drawable.app_splash).into(deviceImage);
                deviceInfo.setOnClickListener {
                    val intent = Intent(context, DetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.DEVICE_ITEM, item)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
                llDevice.setOnClickListener {
                    val intent = Intent(context, DetailsActivity::class.java)
                    val bundle = Bundle()
                    bundle.putSerializable(Constants.DEVICE_ITEM, item)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return devicesListFiltered.size
    }

    /**
     * Updates the data in the adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(list: ArrayList<DevicesModal>) {
        if(list.isNotEmpty())
        {
            devicesList.clear()
            devicesListFiltered.clear()
        }
        devicesList.addAll(list)
        devicesListFiltered.addAll(devicesList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ListDeviceItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Filter the list of items, when user searches.
     */
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString()!!.lowercase(Locale.getDefault()) ?: ""
                devicesListFiltered = if (charString.isEmpty()) devicesList else {
                    val filteredList = ArrayList<DevicesModal>()
                    devicesList
                        .filter {
                            (it.title.toString().lowercase(Locale.getDefault())
                                .contains(constraint.toString().lowercase(Locale.getDefault())))
                        }
                        .forEach { filteredList.add(it) }
                    filteredList
                }
                return FilterResults().apply { values = devicesListFiltered }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                devicesListFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<DevicesModal>
                notifyDataSetChanged()
            }
        }
    }

}