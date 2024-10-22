package app.pay.pandapro.fragments.recharge

import androidx.navigation.fragment.findNavController
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.databinding.FragmentRechargeHostBinding


class RechargeHostFragment : BaseFragment<FragmentRechargeHostBinding>(FragmentRechargeHostBinding::inflate) {
    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setData() {

    }

}