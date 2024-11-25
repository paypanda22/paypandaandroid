package app.pay.retailers.fragments.bbps

import android.content.Intent
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.DashBoardActivity
import app.pay.retailers.databinding.FragmentBbpsStartBinding


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