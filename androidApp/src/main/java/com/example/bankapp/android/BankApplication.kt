package com.example.bankapp.android

import android.app.Application
import com.example.bankapp.di.SharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BankApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BankApplication)
            modules(SharedModule)
        }
    }
}
