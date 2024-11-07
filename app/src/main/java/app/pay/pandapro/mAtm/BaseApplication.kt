package app.pay.pandapro.mAtm

import android.app.Application


class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SSLConfig.init(this)
    /*    val koin: KoinApplication = create(this)
            .modules(matmModule)
        startKoin(koin)*/

    }
}