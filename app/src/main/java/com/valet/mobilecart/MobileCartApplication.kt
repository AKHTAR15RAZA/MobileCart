package com.valet.mobilecart

import android.app.Application
import com.valet.mobilecart.cart.viewmodel.HomeViewModelFactory
import com.valet.mobilecart.network.RetrofitCommon
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MobileCartApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MobileCartApplication))

        bind() from singleton { RetrofitCommon }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from provider { instance() }
    }
}