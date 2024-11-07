package app.pay.pandapro.fragments.aepsFragments

import android.content.Intent
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.DashBoardActivity
import app.pay.pandapro.databinding.FragmentAepsMainBinding


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