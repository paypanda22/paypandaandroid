package app.pay.panda.mAtm

import android.app.Application
import com.sdk.matmsdk.di.matmModule
import org.koin.android.java.KoinAndroidApplication.create
import org.koin.core.KoinApplication
import org.koin.core.context.GlobalContext.startKoin


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val koin: KoinApplication = create(this)
            .modules(matmModule)
        startKoin(koin)
    }
}