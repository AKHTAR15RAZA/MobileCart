package com.valet.mobilecart.cart.view

import android.os.Bundle
import android.view.Menu
import com.bumptech.glide.Glide
import com.valet.mobilecart.R
import com.valet.mobilecart.base.BaseActivity
import com.valet.mobilecart.cart.modal.DevicesModal
import com.valet.mobilecart.databinding.ActivityDetailsBinding
import com.valet.mobilecart.utils.Constants


/**
 * Details page of the application is represented by the DetailsActivity class
 */
class DetailsActivity : BaseActivity() {

    /**
     * declaration of variables
     */
    private lateinit var binding: ActivityDetailsBinding
    private lateinit var devicesModal: DevicesModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        /**
         * load data from intent
         */
        if (intent.getSerializableExtra(Constants.DEVICE_ITEM) != null) {
            devicesModal = (intent.getSerializableExtra(Constants.DEVICE_ITEM) as DevicesModal?)!!
        }

        /**
         * set data from deviceModal on the UI
         */
        binding.apply {
            devicesModal.let {
                val deviceRatings: Float = it.ratings!! as? Float ?: it.ratings.toString().toFloat()
                setTitle(it.title)
                txtOSDevice.text = it.deviceOS
                txtSizeDevice.text = it.design
                txtStatusDevice.text = it.status
                txtDescription.text = it.description
                Glide.with(this@DetailsActivity).load(it.imageUrl).placeholder(R.drawable.app_splash).into(imgDetailsDevice);
                ratingDevice.rating = deviceRatings as? Float ?: 0.0F
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}