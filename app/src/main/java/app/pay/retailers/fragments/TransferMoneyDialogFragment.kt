import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import app.pay.retailers.R
import app.pay.retailers.helperclasses.ShowDialog.Companion.bottomDialogSingleButton
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.responsemodels.moneytransfer.MoneyTransferResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson

class TransferMoneyDialogFragment : DialogFragment() {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var viewlist: MutableList<app.pay.retailers.responsemodels.moneytransfer.Data>
    private var walletAmt = "0"
    private var lock_amt = "0"

    companion object {
        private const val ARG_VALUE = "value"
        private const val ARG_REFER_ID = "refer_id"
        private const val ARG_BALANCE = "balance"
        private const val MOBILE = "mobile"
        private const val NAME = "name"

        fun newInstance(value: String,id:String,balance:String,mobile:String,name:String): TransferMoneyDialogFragment {
            val fragment = TransferMoneyDialogFragment()
            val args = Bundle()
            args.putString(ARG_VALUE, value)
            args.putString(ARG_REFER_ID, id)
            args.putString(ARG_BALANCE, balance)
            args.putString(MOBILE, mobile)
            args.putString(NAME, name)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_transfer_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSession = UserSession(requireContext())
        walletAmt= userSession.getData(Constant.MAIN_WALET).toString()
        lock_amt= userSession.getData(Constant.LOCK_AMT).toString()
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)
        val mobileMo = view.findViewById<TextView>(R.id.mobile)
        val edtTPin = view.findViewById<EditText>(R.id.edtTPin)
        val llTPin = view.findViewById<LinearLayout>(R.id.llTPin)
        val fund = view.findViewById<EditText>(R.id.fund)
        val btnTransfer = view.findViewById<Button>(R.id.btnTransfer)
        val refer_id = view.findViewById<TextView>(R.id.refer_id)
        val NAme = view.findViewById<TextView>(R.id.NAme)

        // Set the balance text (you can pass this value as an argument)
        val balance = arguments?.getString(ARG_BALANCE)
        val referId = arguments?.getString(ARG_REFER_ID)
        val value = arguments?.getString(ARG_VALUE)
        val  mobile = arguments?.getString(MOBILE)
        val  name = arguments?.getString(NAME)

        refer_id.text = "Refer ID:-  $referId"
        mobileMo.text = "Mob:-  $mobile"
        NAme.text = "Name:-  $name"
        val walletAmtDouble = walletAmt.toDoubleOrNull() ?: 0.0
        val lockingDouble = lock_amt.toDoubleOrNull() ?: 0.0
        val available_bal = walletAmtDouble - lockingDouble

        tvBalance.text = "Available Balance:-  $available_bal"
        fund.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                llTPin.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                while (count == 2)
                    llTPin.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                llTPin.visibility = View.VISIBLE
                val input = s.toString().toIntOrNull() ?: 0  // Convert input to integer or use 0 if empty

                when {
                    input < 100 -> {
                        fund.error = "Minimum value is 100"
                    }
                    input > 1000000 -> {
                        fund.error = "Maximum value is 100000"
                    }
                    else -> {
                        fund.error = null  // Clear any error if within range
                    }
                }
            }
        })

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        // Handle button click
        btnTransfer.setOnClickListener {
            val  amount=fund.text.toString()
            if(amount == null || amount < 100.toString()) {
                fund.error = "Minimum amount is 100"
                Toast.makeText(activity, "Please enter at least 100", Toast.LENGTH_SHORT).show()
            } else if(fund.text.toString().isEmpty()){
                Toast.makeText(activity, "Please Enter Amount", Toast.LENGTH_SHORT).show()
            }else if(edtTPin.text.toString().isEmpty()){
                Toast.makeText(activity, "Please Enter TPin", Toast.LENGTH_SHORT).show()
            } else {
                moneyTreansfer(
                    requireActivity(),
                    token,
                    fund.text.toString(),
                    userSession.getData(Constant.M_LONG).toString(),
                    userSession.getData(Constant.M_LAT).toString(),
                    edtTPin.text.toString(),
                    "transfer",
                    value.toString()
                )
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
                    bottomDialogSingleButton(activity, "SUCCESS", response.message, "success", object : MyClick {
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


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
