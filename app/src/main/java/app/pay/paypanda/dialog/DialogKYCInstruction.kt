package app.pay.paypanda.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import app.pay.paypanda.R
import app.pay.paypanda.fragments.onboarding.OnboardingStatusFragment
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.retrofit.Constant

class DialogKYCInstruction : DialogFragment() {
    private lateinit var userSession: UserSession
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_dialog_k_y_c_instruction)
        dialog.setCancelable(false) // Makes the dialog non-cancelable
        userSession = UserSession(requireContext())
        val userType = userSession.getData(Constant.USER_TYPE_NAME)
        val btnAgree = dialog.findViewById<Button>(R.id.btnAgree)
        btnAgree.setOnClickListener {
            if(userType.equals("zsm")|| userType.equals("asm")) {
                (parentFragment as? OnboardingStatusFragment)?.onStepButtonZSMClick(
                    3,
                    requireContext()
                )
                dismiss()
            }else{
                (parentFragment as? OnboardingStatusFragment)?.onStepButtonClick(
                    6,
                    requireContext()
                )
                dismiss()
            }
        }

        return dialog
    }
}
