package app.pay.paypanda.interfaces

interface MCallBackResponse {
    fun success(from: String, message: String)
    fun fail(from: String)
}