package app.pay.paypanda.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import app.pay.paypanda.R
import app.pay.paypanda.helperclasses.ShowDialog.Companion.bottomDialogSingleButton
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.helperclasses.Utils.Companion.showToast
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.responsemodels.fundreverseresponse.FundReverseResendOtpResponse
import app.pay.paypanda.responsemodels.fundreverseverify.FundReverseVerifyResponse
import app.pay.paypanda.responsemodels.moneytransfer.MoneyTransferResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.gson.Gson

class ReverseMoneyDialogFragment : DialogFragment() {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var viewlist: MutableList<app.pay.paypanda.responsemodels.moneytransfer.Data>
    private lateinit var otp: TextView // Declare otp here for easy access
    private lateinit var verifyOTP: TextView
    private lateinit var linVerifyotp: LinearLayout
    private lateinit var relativeResend: RelativeLayout
    private lateinit var tvResendOtp: TextView
    private lateinit var tvTimer: TextView
    var timeLeftInMillis: Long = 60000L
    var authToken:String=""
    var isTimerRunning: Boolean = false
    private lateinit var countDownTimer: CountDownTimer
    var varifytoken:String=""
    companion object {
        private const val ARG_VALUE = "value"
        private const val ARG_REFER_ID = "refer_id"
        private const val ARG_BALANCE = "balance"

        fun newInstance(value: String,id:String,balance:String): ReverseMoneyDialogFragment {
            val fragment = ReverseMoneyDialogFragment()
            val args = Bundle()
            args.putString(ARG_VALUE, value)
            args.putString(ARG_REFER_ID, id)
            args.putString(ARG_BALANCE, balance)
            fragment.arguments = args
            return fragment
        }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_reverse_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSession = UserSession(requireContext())
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)
        val edtTPin = view.findViewById<EditText>(R.id.edtTPin)
        val fund = view.findViewById<EditText>(R.id.fund)
        val btnTransfer = view.findViewById<Button>(R.id.btnTransfer)
        val refer_id = view.findViewById<TextView>(R.id.refer_id)
         otp = view.findViewById<EditText>(R.id.otp)
        verifyOTP = view.findViewById<TextView>(R.id.verifyOTP)
        linVerifyotp = view.findViewById<LinearLayout>(R.id.linVerifyotp)
        tvResendOtp = view.findViewById<TextView>(R.id.tvResendOtp)
        tvTimer = view.findViewById<TextView>(R.id.tvTimer)
        relativeResend = view.findViewById<RelativeLayout>(R.id.relativeResend)

        // Set the balance text (you can pass this value as an argument)
        val balance = arguments?.getString(ARG_BALANCE)
        val referId = arguments?.getString(ARG_REFER_ID)
        val value = arguments?.getString(ARG_VALUE)
        tvBalance.text = "Balance:-$balance"
        refer_id.text = "Refer ID:-$referId"
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        fund.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                edtTPin.visibility = View.GONE

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                while (count == 2)
                    edtTPin.visibility = View.GONE

            }

            override fun afterTextChanged(s: Editable?) {
                edtTPin.visibility = View.VISIBLE

            }
        })


        // Handle button click
        btnTransfer.setOnClickListener {
            if (TextUtils.isEmpty(edtTPin.text)) {
                Toast.makeText(requireContext(), "Please Enter Tpin", Toast.LENGTH_SHORT).show()
            }  else {
                moneyTreansfer(
                    requireActivity(),
                    token,
                    fund.text.toString(),
                    userSession.getData(Constant.M_LONG).toString(),
                    userSession.getData(Constant.M_LAT).toString(),
                    edtTPin.text.toString(),
                    "refund",
                    value.toString()
                )
            }
        }
        verifyOTP.setOnClickListener{
             if(TextUtils.isEmpty(otp.text.toString())){
            Toast.makeText(requireContext(), "Please Verify OTP First", Toast.LENGTH_SHORT).show()
            }else {
                 otpVarify(
                     requireActivity(),
                     token,
                     otp.text.toString(),
                     userSession.getData(Constant.M_LONG).toString(),
                     userSession.getData(Constant.M_LAT).toString(),
                 )
             }
        }

      tvResendOtp.setOnClickListener {
          if (isTimerRunning) {
              Toast.makeText(activity, "You can Resend OTP After One Minute", Toast.LENGTH_SHORT)
                  .show()
          } else {
              timeLeftInMillis = 60000
              setTimer()
              fundReverseResendOtp(fund.text.toString())

          }
      }
    }


    private fun moneyTreansfer(activity: FragmentActivity, token: String, amount: String, lat: String, long: String,tpin:String,trans_type:String,transferTo:String) {
        val requestData = hashMapOf<String, Any?>()
        requestData["token"] = token
        requestData["amount"] = amount
        requestData["lat"] = lat
        requestData["long"] =long
        requestData["tpin"] = tpin
        requestData["trans_type"] = trans_type
        requestData["transferTo"] = transferTo
        UtilMethods.moneyTreansfer(activity, token,requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: MoneyTransferResponse = Gson().fromJson(message, MoneyTransferResponse::class.java)
                if (!response.error) {
                    authToken=response.data.token
                    varifytoken=response.data.token
                    otp.visibility=View.VISIBLE
                    verifyOTP.visibility=View.VISIBLE
                    linVerifyotp.visibility=View.VISIBLE
                    relativeResend.visibility=View.VISIBLE

                } else {
                    showToast(activity, response.message)
                }
            }

            override fun fail(from: String) {
                showToast(activity, from)
            }
        })

    }
    private fun otpVarify(activity: FragmentActivity, token: String,otp:String, lat: String, long: String) {
        val requestData = hashMapOf<String, Any?>()
        requestData["token"] = varifytoken
        requestData["otp"] = otp
        requestData["lat"] = lat
        requestData["long"] = long

        UtilMethods.otpVarify(activity, token, requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: FundReverseVerifyResponse =
                    Gson().fromJson(message, FundReverseVerifyResponse::class.java)
                if (!response.error) {
                    bottomDialogSingleButton(activity, "SUCCESS", response.message.toString(), "success", object : MyClick {
                        override fun onClick() {
                            dismiss()
                        }
                    })
                } else {
                    showToast(activity, response.message)
                }
            }

            override fun fail(from: String) {
                showToast(activity, from)
            }
        })
    }


    private fun fundReverseResendOtp(amount:String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["token"] = authToken
         requestData["amount"]=amount

        UtilMethods.fundReverseResendOtp(requireContext(),token ,requestData,object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: FundReverseResendOtpResponse =
                    Gson().fromJson(message, FundReverseResendOtpResponse::class.java)
                if (response.error == false) {
                    Toast.makeText(
                        requireContext(),
                        "Otp send successfully to your mobile number",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        requireContext(),
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(), from)
            }
        })
    }

    fun setTimer() {
        tvTimer.visibility = View.VISIBLE
      tvResendOtp.setTextColor(
            ContextCompat.getColor(
                requireActivity(), R.color.bggrey_dark
            )
        )
        countDownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
              tvResendOtp.isEnabled = true
               tvTimer.visibility = View.GONE
              tvResendOtp.setTextColor(
                    ContextCompat.getColor(
                        requireActivity(), R.color.colorPrimaryDark
                    )
                )
                isTimerRunning = false
            }
        }
        countDownTimer.start()
        isTimerRunning = true
    }

    private fun cancelTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel()
        }

    }

    private fun updateTimer() {
        val minutes = timeLeftInMillis / 1000 / 60
        val seconds = timeLeftInMillis / 1000 % 60

        @SuppressLint("DefaultLocale") val timeLeftFormatted =
            String.format("%02d:%02d", minutes, seconds)
        tvTimer.text = timeLeftFormatted
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
    override fun onPause() {
        super.onPause()
        if (isTimerRunning) {
            cancelTimer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!this::countDownTimer.isInitialized) {
            setTimer() // Initialize and start the timer
        } else {
            countDownTimer.start() // Start the timer if already initialized
        }
    }



}
