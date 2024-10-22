package app.pay.pandapro.fragments.onboarding

import android.app.AlertDialog
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.databinding.FragmentPendingApprovalBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.OnBackPressedListner


class PendingApprovalFragment : BaseFragment<FragmentPendingApprovalBinding>(FragmentPendingApprovalBinding::inflate),OnBackPressedListner {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())


    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
    /*    binding.ivBack.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit !")
                .setNegativeButton("Logout") { dialog, which ->
                    activity?.let { it1 -> userSession.logoutUser(it1) }
                }
                .setPositiveButton("Exit") { dialog, which ->
                    activity?.finishAffinity()
                }.show()
        }*/
        binding.btnSubmit.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are You Sure You Want to Exit?")
                .setTitle("Exit !")
                .setNegativeButton("Logout") { dialog, which ->
                    activity?.let { it1 -> userSession.logoutUser(it1) }
                }
                .setPositiveButton("Exit") { dialog, which ->
                    activity?.finishAffinity()
                }.show()
        }

    }

    override fun setData() {

    }

    override fun backPressed() {
        val builder = AlertDialog.Builder(myActivity)
        builder.setMessage("Are You Sure You Want to Exit?")
            .setTitle("Exit !")
            .setNegativeButton("Logout") { dialog, which ->
                userSession.logoutUser(myActivity)
            }
            .setPositiveButton("Exit") { dialog, which ->
                myActivity.finishAffinity()
            }.show()
    }

}