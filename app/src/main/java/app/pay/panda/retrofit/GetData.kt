package app.pay.panda.retrofit


import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GetData {

    @GET("api/auth/isMobileNoExist/{mobile}")
    fun chkMobile(@Path("mobile") mobile: String): Call<JsonObject>

    @GET("api/auth/isEmailexist/{email}")
    fun chkEmail(@Path("email") email: String): Call<JsonObject>

    @POST("api/auth/mb/login")
    fun userLogin(@Body obj: Any): Call<JsonObject>

    @POST("api/auth/mb/verifyMobileNo")
    fun sendOtpMobile(@Body obj: Any): Call<JsonObject>

    @POST("api/auth/mb/verifyMobileNoOtp")
    fun verifyOtp(@Body obj: Any): Call<JsonObject>

    @POST("api/auth/mb/verifyEmail")
    fun sendOtpEmail(@Body obj: Any): Call<JsonObject>

    @POST("api/auth/mb/verifyEmailOtp")
    fun verifyOtpEmail(@Body obj: Any): Call<JsonObject>

    @GET("api/auth/mb/isDistributorExist")
    fun checkSponsorCode(@Query("refer_id") refer_id: String): Call<JsonObject>

    @POST("api/auth/mb/register")
    fun register(@Body obj: Any): Call<JsonObject>

    @GET("api/auth/userValidate")
    fun dashBoardData(): Call<JsonObject>

    @POST("api/verification/pan/verifypanOtp")
    fun verifyPan(@Body obj: Any): Call<JsonObject>

    @POST("api/verification/adhaar/otp")
    fun aadhaarSendOTP(@Body obj: Any): Call<JsonObject>

    @POST("api/verification/adhaar/verify")
    fun aadhaarVerifyOtp(@Body obj: Any): Call<JsonObject>

    @GET("api/state/country/{country_id}")
    fun getStateList(@Path("country_id") countryID: String): Call<JsonObject>

    @GET("api/country/public/list")
    fun getCountryList(): Call<JsonObject>

    @POST("api/verification/personalDetails")
    fun updatePersonalDetails(@Body obj: Any): Call<JsonObject>

    @POST("api/verification/gstNo")
    fun verifyGst(@Body obj: Any): Call<JsonObject>

    @POST("api/verification/gstNoSave")
    fun updateGstDetails(@Body obj: Any): Call<JsonObject>

   // @POST("api/verification/bankAccount")
    @POST("api/verification/bankVerification")
    fun verifyBankAccount(@Body obj: Any): Call<JsonObject>

    @Multipart
    @POST("api/cloudinary/addImage")
    fun uploadImage(@Part image: MultipartBody.Part): Call<JsonObject>

    @POST("api/userdocument/add_doc")
    fun submitDocs(@Body obj: Any): Call<JsonObject>

    @GET("api/dmt_bank/public/list")
    fun dmtBankList(): Call<JsonObject>

    @POST("api/verification/bankAccountSave")
    fun saveBankDetails(@Body obj: Any): Call<JsonObject>

    @Multipart
    @POST("api/verification/kycUpload")
    fun uploadKycVideo(@Part video: MultipartBody.Part, @Part("user_id") token: RequestBody): Call<JsonObject>

    @POST("api/verification/docSave")
    fun selfDeclaration(@Body obj: Any): Call<JsonObject>

    @GET("api/eko/customerProfile")
    fun getDmtCustomerInfo(@Query("mobileNo") mobileNo: String, @Query("api_id") apiId: String): Call<JsonObject>

    @GET("api/eko/recipientList")
    fun getBeneficiaryList(@Query("mobileNo") mobileNo: String, @Query("api_id") apiId: String): Call<JsonObject>

    @POST("api/dmt_txn/trans")
    fun dmtMakeTransaction(@Body obj: Any): Call<JsonObject>

    @POST("api/dmt_txn/report")
    fun dmtTransactionList(@Body obj: Any): Call<JsonObject>

    @POST("api/eko/createCustomer")
    fun createCustomer(@Body obj: Any): Call<JsonObject>

    @POST("api/eko/resendCustomerOtp")
    fun resendCustomerOtp(@Body obj: Any): Call<JsonObject>

    @POST("api/eko/verifyCustomer")
    fun verifyCustomerOtp(@Body obj: Any): Call<JsonObject>

    @POST("api/eko/recipientAdd")
    fun addEkoRecipient(@Body obj: Any): Call<JsonObject>

    @GET("api/operator/state/{categoryID}")
    fun getOperator(@Path("categoryID") categoryID: String): Call<JsonObject>

    @GET("api/dmt_txn/trans/{txId}")
    fun dmtTxnEnquiry(@Path("txId") txId: String): Call<JsonObject>

    @POST("api/dmt_txn/refund/initialize")
    fun dmtInitiateRefund(@Body obj: Any): Call<JsonObject>

    @GET("api/dmt_txn/batchId/{batchId}")
    fun getTxnByBatchId(@Path("batchId") batchId: String): Call<JsonObject>

    @POST("api/auth/mb/login/check")
    fun passwordCheck(@Body obj: Any): Call<JsonObject>

    @PUT("api/auth/mb/resetPassword")
    fun forgetPassword(@Body obj: Any): Call<JsonObject>

    @GET("api/bank/public")
    fun getCompanyBankList(): Call<JsonObject>

    @GET("api/setting/bankVerify")
    fun getDmtSettings(): Call<JsonObject>

    @POST("api/dmt_txn/refund/verify")
    fun processDmtRefundWithOtp(@Body obj: Any): Call<JsonObject>

    @POST("api/mainwallet/public/filter")
    fun walletTxnReport(@Body obj: Any): Call<JsonObject>

    @GET("api/paymentRequestToUser/permit")
    fun getTransferTo(): Call<JsonObject>

    @POST("api/paymentRequest/addrequest")
    fun addPaymentRequest(@Body obj: Any): Call<JsonObject>

   /* @POST("api/paymentRequestToUser/user")*/
    @POST("api/paymentRequest/user/all")
    fun walletRequestList(@Body obj: Any): Call<JsonObject>

    @POST("api/paymentRequestToUser/user/all")
    fun walletRequestListDist(@Body obj: Any): Call<JsonObject>

    @GET("api/service_user_permission/isAvail/{serviceID}")
    fun isServiceAvailable(@Path("serviceID") serviceID: String): Call<JsonObject>

    //AEPS APIS
    @POST("api/aeps/bankRegister")
    fun aepsAuthRegister(@Body obj: Any): Call<JsonObject>

    @POST("api/aeps/bankAuth")
    fun aepsDailyAuth(@Body obj: Any): Call<JsonObject>

    @GET("https://api.paypandabnk.com/api/aeps/bankList")
    fun aepsBankList(): Call<JsonObject>

    @POST("api/aeps/banktxnMerchantAuth")
    fun merchantCwAuth(@Body obj: Any): Call<JsonObject>

    @POST("api/aeps/bankWithdraw")
    fun aepsCashWithdrawal(@Body obj: Any): Call<JsonObject>

    @GET("api/deliveryaddress/permanentAdd")
    fun getUserAadhaarAddress(): Call<JsonObject>

    @POST("api/aeps/balanceVerify")
    fun aepsBalanceEnquiry(@Body obj: Any): Call<JsonObject>

    @POST("api/aeps/AepsMiniStatement")
    fun aepsMiniStatement(@Body obj: Any): Call<JsonObject>

    @POST("api/aeps/adhaarPay")
    fun aepsAadhaarPay(@Body obj: Any): Call<JsonObject>

    @POST("api/aepsTxn/Public")
    fun aepsTransactionList(@Body obj: Any): Call<JsonObject>

    @PUT("api/user/userProfile")
    fun updateProfileImage(@Body obj: Any): Call<JsonObject>

    @PUT("api/auth/passwordChange")
    fun changePassword(@Body obj: Any): Call<JsonObject>

    @PUT("api/auth/pinChange")
    fun changePin(@Body obj: Any): Call<JsonObject>

    @GET("api/auth/forgotPinSendOtp")
    fun forgetPinSendOtp(): Call<JsonObject>

    @PUT("api/auth/forgotPinVerifyOtp")
    fun forgetTransactionPin(@Body obj: Any): Call<JsonObject>

    @GET("api/notification/filter/public")
    fun getNotifications(@Query("count") count: String, @Query("readed") readed: Boolean): Call<JsonObject>

    @GET("api/operator/public")
    fun getBbpsOperators(@Query("api_id") api_id: String, @Query("service") service: String): Call<JsonObject>

    @POST("api/billPayment/billDetail")
    fun fetchBill(@Body obj: Any): Call<JsonObject>

    @POST("api/billPayment/paybill")
    fun payBill(@Body obj: Any): Call<JsonObject>

    @GET("api/dmtdisputes/allMasters")
    fun getDisputeMasters(): Call<JsonObject>

    @POST("api/dmtdisputes/add_dispute")
    fun addSupportTicket(@Body obj: Any): Call<JsonObject>

    @POST("api/dmtdisputes/public")
    fun getSupportTicketList(@Body obj: Any): Call<JsonObject>

    @POST("api/dmtDisputeChat/add_dispute/public")
    fun replyToTicket(@Body obj: Any): Call<JsonObject>

    @GET("api/bbps_txn/public")
    fun getUtilityTransactions(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String,
        @Query("biller_id") biller_id: String,
        @Query("page") page: String,
        @Query("count") count: String,
        @Query("category_id") category_id: String
    ): Call<JsonObject>

    @GET("api/package/public")
    fun getPackageList(): Call<JsonObject>

    @POST("api/packagePayment/pay")
    fun purchasePackage(@Body obj: Any): Call<JsonObject>

    @POST("api/cms/general")
    fun getCmsUrl(@Body obj: Any): Call<JsonObject>

    @GET("api/recharge_txn/operator")
    fun getNumberDetails(@Query("mobileNo") mobileNo: String): Call<JsonObject>

    @GET("api/recharge_txn/rechargeOperators")
    fun getRechargeOperators(@Query("servicetypeid") servicetypeid: String): Call<JsonObject>

    @GET("api/recharge_txn/mobilePlanDetails")
    fun getRechargePlans(@Query("mobileNo") mobileNo: String): Call<JsonObject>

    @POST("api/recharge_txn/payBill")
    fun processRecharge(@Body obj: Any): Call<JsonObject>

    @GET("api/recharge_txn/dthPlanDetails")
    fun getDthInfo(
        @Query("operator_code") operatorId: String,
        @Query("mobileNo") mobileNo: String
    ): Call<JsonObject>

    @POST("api/aeps/cashdeposit")
    fun aepsCashDeposit(@Body obj:Any):Call<JsonObject>

    @GET("api/aeps/cashDepositBankList")
    fun cashDepositBanks():Call<JsonObject>

    @POST("api/quickDhan/sendotp")
    fun quickDhanSendOtp(@Body obj:Any):Call<JsonObject>

    @GET("api/payout/Benefiaries")
    fun getPayoutBankList():Call<JsonObject>

    @POST("api/auth/verifyEmailAndMobileNo")
    fun getRegistrationOtp(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/verifyOtpBothEmailAndMobileNo")
    fun verifyRegisterOtp(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/mb/verifyLoginOtp")
    fun newLoginOtp(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/forgotPassInitiateByMobile")
    fun newForgetPasswordMobile(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/forgotPassInitiateByEmail")
    fun newForgetPasswordEmail(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/fogotPassVerifyByEmail")
    fun forgetPasswordOtpEmail(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/forgotPassVerifyByMobile")
    fun forgetPasswordOtpMobile(@Body obj:Any):Call<JsonObject>

    @POST("api/auth/onboardingRequest")
    fun registrationRequest(@Body obj:Any):Call<JsonObject>

    @GET("api/auth/generateOtpForTpin")
    fun generateOtpForTPin():Call<JsonObject>

    @POST("api/auth/varifyOtpForTpin")
    fun verifyTPinOtp(@Body obj: Any):Call<JsonObject>

    @POST("api/auth/genNewTpin")
    fun createNewTPin(@Body obj: Any):Call<JsonObject>

    @POST("api/aepsTransfer/transfer")
    fun transferAepsToMain(@Body obj:Any):Call<JsonObject>

    @GET("api/bbps_txn/allBbpsOperator")
    fun utilityReport():Call<JsonObject>

    @GET("api/usertype/public/list")
    fun userType():Call<JsonObject>

    @POST("api/auth/onboardingRequest")
    fun obBoardingRequest(@Body obj: JsonObject):Call<JsonObject>

    @POST("api/payout/addAccount")
    fun savePayoutDetails(@Body obj: Any): Call<JsonObject>

    @GET("api/bbps_txn/getBbpsService")
    fun getBbpsServiceId
                (@Query("service_id") serviceId: String
    ): Call<JsonObject>

    @GET("api/bbps_txn/invoice/{id}")
    fun getTxnByUtilityId(@Path("id") id: String): Call<JsonObject>

    @GET("/api/user/referTo")
    fun getNetwork(
        @Query("page") page: String,
        @Query("count") count: String
    ): Call<JsonObject>

    @GET("/api/user/referTo")
    fun getNetworkRetailer(
        @Query("page") page: String,
        @Query("count") count: String,
        @Query("id") id: String,
    ): Call<JsonObject>

    @GET("/api/report/fundtransfer")
    fun viewReport(
        @Query("page") page: String,
        @Query("count") count: String,
        @Query("to") to: String,
    ): Call<JsonObject>

    @POST("/api/user/fundTransferToRefer")
    fun moneyTreansfer(@Body obj:Any): Call<JsonObject>

    @POST("/api/user/fundReverseVerify")
    fun otpVarify(@Body obj:Any): Call<JsonObject>

    @POST("/api/mainwallet/public/filter")
    fun earningReport(@Body obj:Any): Call<JsonObject>

    @GET("/api/dashboard/public")
    fun distributerDashboard(@Query("date") date: String
    ): Call<JsonObject>

    @GET("/api/auth/mb/get/profile")
    fun getUserDetail(): Call<JsonObject>

    @POST("api/auth/mb/login/resendOtp")
    fun resendOTP(@Body obj: Any): Call<JsonObject>

    @PUT("api/paymentrequesttouser/transfer")
    fun paymentRequestToUser(@Body obj: Any): Call<JsonObject>

    @POST("api/paymentRequestToUser/admin")
    fun paymentRequestToAdmin(@Body obj: Any): Call<JsonObject>




    @GET("api/auth/resendOtpForTpin")
    fun resendOtpForTPin(): Call<JsonObject>
    @GET("api/auth/resendOtp")
    fun resendOtpTPin(@Body obj: Any): Call<JsonObject>


    @GET("api/package/public/{id}")
    fun packageDatails(@Path("id") id: String): Call<JsonObject>
    @GET("api/package/bbps/commision")
    fun bbpsServices(@Query("package_id") package_id: String,
                     @Query("service_id") service_id: String,
                     @Query("page") page: String,
                     @Query("count") count: String): Call<JsonObject>


    @GET("api/auth/recipientDelete")
    fun recipientDelete(@Body obj: Any): Call<JsonObject>








    //YooPayAPIs
    @POST("api/general/check/{pincode}")
    fun getPinCodeData(@Path("pincode") pincode: String): Call<JsonObject>

    @FormUrlEncoded
    @POST("api/general/xml")
    fun checkFData(@Field("xmlData") fData: String): Call<JsonObject>




}