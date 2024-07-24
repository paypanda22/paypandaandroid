package app.pay.panda.fragments.onboarding

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.FOCUSABLE
import android.view.View.FOCUSABLE_AUTO
import android.view.View.GONE
import android.view.View.NOT_FOCUSABLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentVerifyIdentityBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.verifypan.VerifyPanResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class VerifyIdentityFragment : BaseFragment<FragmentVerifyIdentityBinding>(FragmentVerifyIdentityBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private var refID = ""

    @SuppressLint("SetTextI18n")
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        if (userSession.getBoolData(Constant.PAN_VERIFIED)) {
            binding.edtPanNumber.setText(userSession.getData(Constant.PAN_NUMBER).toString())
            binding.lytPanNumber.visibility= GONE
            binding.tvBtnPan.visibility= VISIBLE
            binding.tvBtnPan.text= userSession.getData(Constant.PAN_NUMBER).toString()
            binding.tvPanVerifiedName.text = userSession.getData(Constant.PAN_NAME).toString()
            binding.llPanName.visibility = View.VISIBLE
            binding.tvEditPan.visibility= VISIBLE
            binding.btnPan.text = "NEXT"
        }

    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    @SuppressLint("SetTextI18n")
    override fun addListeners() {
        binding.edtPanNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.edtPanNumber.text.toString().length == 10) {
                    if (ActivityExtensions.isValidPan(binding.edtPanNumber.text.toString())) {
                        binding.edtPanNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_green, 0)
                    }else{
                        binding.edtPanNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }else{
                    binding.edtPanNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnPan.setOnClickListener {
            if (binding.edtPanNumber.text.toString().length != 10) {
                binding.edtPanNumber.error = "Enter a Valid Pan Number"
            } else {
                if (!ActivityExtensions.isValidPan(binding.edtPanNumber.text.toString())) {
                    binding.edtPanNumber.error = "Enter a Valid Pan Number"
                } else {
                    if (!userSession.getBoolData(Constant.PAN_VERIFIED)) {
                        validatePan()
                    } else {
                        findNavController().navigate(R.id.action_verifyIdentityFragment_to_adhaarVerificationFragment)
                    }
                }

            }
        }
        
        binding.tvEditPan.setOnClickListener {
            val builder = AlertDialog.Builder(myActivity)
            builder.setMessage("Do you want to Edit Pan Number ?")
                .setTitle("Edit Pan Number !")
                .setNegativeButton("YES") { _, _ ->
                    userSession.setBoolData(Constant.PAN_VERIFIED,false)
                    binding.lytPanNumber.visibility= VISIBLE
                    binding.tvBtnPan.visibility= GONE
                }
                .setPositiveButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }

    }


    private fun validatePan() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["pan"] = binding.edtPanNumber.text.toString()
        requestData["user_id"] = token
        UtilMethods.verifyPan(requireContext(), requestData, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                val response: VerifyPanResponse = Gson().fromJson(message, VerifyPanResponse::class.java)
                userSession.setBoolData(Constant.PAN_VERIFIED, true)
                binding.llPanName.visibility = View.VISIBLE
                binding.tvPanVerifiedName.text = response.data?.name.toString()
                binding.btnPan.text = "NEXT"

            }

            override fun fail(from: String) {
                Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun setData() {

    }

//    fun backPressed() {
//        if (binding.rlAadhaar.visibility==View.VISIBLE){
//            binding.rlPan.visibility=View.VISIBLE
//            binding.rlAadhaar.visibility=View.GONE
//            binding.tvCurrStep.text="1"
//        }else{
//            findNavController().popBackStack()
//        }
//    }

}







