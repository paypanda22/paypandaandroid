package app.pay.retailers.fragments.recharge

import androidx.navigation.fragment.findNavController
import app.pay.retailers.BaseFragment
import app.pay.retailers.databinding.FragmentRechargeHostBinding


class RechargeHostFragment : BaseFragment<FragmentRechargeHostBinding>(FragmentRechargeHostBinding::inflate) {
    override fun init() {

    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
    }

    override fun setData() {

    }

}