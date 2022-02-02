package com.valet.mobilecart.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.valet.mobilecart.R
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

/**
 * BaseActivity is the parent activity of all the activity inside the application
 * It is extended to apply the same behaviour throughout the application
 * */
open class BaseActivity : AppCompatActivity(), KodeinAware {

    lateinit var loadingDialog: Dialog
    override val kodein by closestKodein()

    /**
     *  @param color : Int - Hex Color
     *  Set color on the toolbar.
     * */
    private fun setToolbarColor(color: Int) {
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    override fun onResume() {
        super.onResume()
        setToolbarColor(R.color.primary)
    }

    override fun onStart() {
        super.onStart()
        loadingDialog = Dialog(this)
        loadingDialog.makeTransparent()
        loadingDialog.setContentView(
            LayoutInflater.from(this).inflate(R.layout.layout_loading, null, false)
        )
    }

    /**
     *  @param cancellable : Boolean (default set to false)
     *  Universal Loading dialog to be shown while making network call/performing heavy operations
     * */
    fun showLoading(cancellable: Boolean = false) {
        loadingDialog.setCancelable(cancellable)
        loadingDialog.show()
    }

    /**
     * To dismiss Loading dialog after completing network call/performing heavy operations
     * */
    fun dismissLoading() {
        if (this::loadingDialog.isInitialized && loadingDialog.isShowing)
            loadingDialog.dismiss()
    }

    /**
     * Removes Notification bar
     * */
    fun hideBar() {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    /**
     *  To make background of loading dialog transparent
     * */
    private inline fun Dialog.makeTransparent(backgroundVisibility: Float = 0f) {
        window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setDimAmount(backgroundVisibility)
        }
    }
}