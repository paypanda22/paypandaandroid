import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import app.pay.panda.R
import app.pay.panda.fragments.ViewReportDialogFragment
import app.pay.panda.helperclasses.ShowDialog.Companion.bottomDialogSingleButton
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.moneytransfer.MoneyTransferResponse
import app.pay.panda.responsemodels.pinChange.PasswordChangeResponse
import app.pay.panda.responsemodels.viewreportdialog.Data
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class TransferMoneyDialogFragment : DialogFragment() {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var viewlist: MutableList<app.pay.panda.responsemodels.moneytransfer.Data>


    companion object {
        private const val ARG_VALUE = "value"
        private const val ARG_REFER_ID = "refer_id"
        private const val ARG_BALANCE = "balance"

        fun newInstance(value: String,id:String,balance:String): TransferMoneyDialogFragment {
            val fragment = TransferMoneyDialogFragment()
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
        return inflater.inflate(R.layout.dialog_transfer_money, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSession = UserSession(requireContext())
        val tvBalance = view.findViewById<TextView>(R.id.tvBalance)
        val edtTPin = view.findViewById<EditText>(R.id.edtTPin)
        val fund = view.findViewById<EditText>(R.id.fund)
        val btnTransfer = view.findViewById<Button>(R.id.btnTransfer)
        val refer_id = view.findViewById<TextView>(R.id.refer_id)

        // Set the balance text (you can pass this value as an argument)
        val balance = arguments?.getString(ARG_BALANCE)
        val referId = arguments?.getString(ARG_REFER_ID)
        val value = arguments?.getString(ARG_VALUE)
        tvBalance.text = "Balance:-$balance"
        refer_id.text = "Refer ID:-$referId"

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

        val token = userSession.getData(Constant.USER_TOKEN).toString()
        // Handle button click
        btnTransfer.setOnClickListener {
            if (TextUtils.isEmpty(edtTPin.text)) {
                Toast.makeText(requireContext(), "Please Enter Tpin", Toast.LENGTH_SHORT)
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
