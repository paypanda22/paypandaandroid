package app.pay.pandapro.fragments.bbps

import android.content.Intent
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.R
import app.pay.pandapro.activity.DashBoardActivity
import app.pay.pandapro.databinding.FragmentBbpsStartBinding


class BbpsStartFragment : BaseFragment<FragmentBbpsStartBinding>(FragmentBbpsStartBinding::inflate) {
    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(activity, DashBoardActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        }
    }

    override fun setData() {

    }

}