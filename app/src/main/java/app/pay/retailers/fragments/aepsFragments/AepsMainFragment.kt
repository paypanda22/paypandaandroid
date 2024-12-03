package app.pay.retailers.fragments.aepsFragments

import android.content.Intent
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.DashBoardActivity
import app.pay.retailers.databinding.FragmentAepsMainBinding


class AepsMainFragment : BaseFragment<FragmentAepsMainBinding>(FragmentAepsMainBinding::inflate) {

    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(activity,DashBoardActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }

    }

    override fun setData() {

    }

}