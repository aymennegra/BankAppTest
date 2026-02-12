package com.example.bankapp.android

import android.app.Application
import com.example.bankapp.android.di.androidModule
import com.example.bankapp.di.SharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class BankApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@BankApplication)
            modules(SharedModule,androidModule)
        }
    }
}
