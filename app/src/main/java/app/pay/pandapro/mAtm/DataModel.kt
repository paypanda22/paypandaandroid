package app.pay.pandapro.mAtm

class ThemeColor(
    var buttonBgColor: String,
    var buttonTextColor: String
)

class DataModel(
    var transactionAmount: Int,
    var transactionType: String,
    var shop_name: String,
    var brand_name: String,
    var applicationType: String,
    var paramB: String,
    var paramC: String,
    var device_type: String,
    var device_name: String,
    var apiUserName: String,
    var user_mobile_number: String,
    private var userName: String,
    private var clientRefID: String,
    private var clientID: String,
    private var clientSecret: String,
    var loginID: String,
    var skipReceipt: Boolean,
    var themeColor: ThemeColor
)



