package app.pay.panda.fragments.recharge

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseActivity
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.databinding.FragmentRechargeHostBinding


class RechargeHostFragment : BaseFragment<FragmentRechargeHostBinding>(FragmentRechargeHostBinding::inflate) {
    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setData() {

    }

}