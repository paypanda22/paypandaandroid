package app.pay.panda.fragments.aepsFragments

import android.content.Intent
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.databinding.FragmentAepsMainBinding


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