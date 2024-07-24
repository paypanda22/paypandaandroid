package app.pay.panda.fragments.dmt

import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.pay.panda.BaseFragment
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentAddBeneficiaryBinding
import app.pay.panda.helperclasses.UserSession


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