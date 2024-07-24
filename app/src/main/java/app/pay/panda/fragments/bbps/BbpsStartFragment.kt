package app.pay.panda.fragments.bbps

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.databinding.FragmentBbpsStartBinding


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