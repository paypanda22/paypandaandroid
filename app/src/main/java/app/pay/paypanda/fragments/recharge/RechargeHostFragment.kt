package app.pay.paypanda.fragments.recharge

import androidx.navigation.fragment.findNavController
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.databinding.FragmentRechargeHostBinding


class RechargeHostFragment : BaseFragment<FragmentRechargeHostBinding>(FragmentRechargeHostBinding::inflate) {
    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setData() {

    }

}