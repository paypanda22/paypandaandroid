package app.pay.retailers.interfaces

interface MCallBackResponse {
    fun success(from: String, message: String)
    fun fail(from: String)
}