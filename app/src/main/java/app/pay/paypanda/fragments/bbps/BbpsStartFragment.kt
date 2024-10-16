package app.pay.paypanda.fragments.bbps

import android.content.Intent
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.DashBoardActivity
import app.pay.paypanda.databinding.FragmentBbpsStartBinding


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