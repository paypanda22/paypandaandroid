package app.pay.retailers.fragments.dmt

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.pay.retailers.BaseFragment
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.databinding.FragmentAddBeneficiaryBinding
import app.pay.retailers.helperclasses.UserSession


class AddBeneficiaryFragment : BaseFragment<FragmentAddBeneficiaryBinding>(FragmentAddBeneficiaryBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    private var apiID=""
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        apiID=arguments?.getString("apiID").toString()
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {

    }

    override fun setData() {

    }

}