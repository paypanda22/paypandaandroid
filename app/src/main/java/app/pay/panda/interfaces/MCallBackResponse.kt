package app.pay.panda.interfaces

interface MCallBackResponse {
    fun success(from: String, message: String)
    fun fail(from: String)
}