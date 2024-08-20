package app.pay.panda.fragments

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import app.pay.panda.R
import app.pay.panda.fragments.onboarding.OnboardingStatusFragment

class DialogKYCInstruction : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_dialog_k_y_c_instruction)
        dialog.setCancelable(false) // Makes the dialog non-cancelable

        val btnAgree = dialog.findViewById<Button>(R.id.btnAgree)
        btnAgree.setOnClickListener {
            (parentFragment as? OnboardingStatusFragment)?.onStepButtonClick(6, requireContext())
            dismiss()
        }

        return dialog
    }
}
