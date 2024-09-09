package app.pay.panda.responsemodels.certificate


import android.net.Uri
import androidx.annotation.Keep

@Keep
data class Data(
    val address: String = "",
    val merchant_type: String = "",
    val name: String = "",
    val onBoardDate: String = "",
    val refer_id: String = "",
    val sign: String ="",
    val validDate: String = ""
)