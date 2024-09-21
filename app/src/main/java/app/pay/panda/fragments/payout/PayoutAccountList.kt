package app.pay.panda.fragments.payout

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R

import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.activity.PayoutActivity
import app.pay.panda.adapters.PayoutAccountAdapter
import app.pay.panda.databinding.FragmentPayoutAccountListBinding
import app.pay.panda.fragments.home.AepsHomeFragment
import app.pay.panda.helperclasses.AepsWalletActions
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.OnBackPressedListner
import app.pay.panda.interfaces.UserBankAccountClick
import app.pay.panda.mAtm.DataModel
import app.pay.panda.mAtm.ThemeColor
import app.pay.panda.responsemodels.deletePayoutBank.DeletePayoutBankAccountResponse
import app.pay.panda.responsemodels.payoutBanks.Data
import app.pay.panda.responsemodels.payoutBanks.PayoutBankListResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonObject


class PayoutAccountList : BaseFragment<FragmentPayoutAccountListBinding>(FragmentPayoutAccountListBinding::inflate),UserBankAccountClick,PayoutAccountAdapter.PayoutAccountOnLongPressed {
    private lateinit var userSession: UserSession
    private lateinit var myActivity:FragmentActivity
    private lateinit var accountList:MutableList<Data>
    private lateinit var wallet:AepsWalletActions
    override fun init() {
        nullActivityCheck()
        userSession=UserSession(requireContext())
        wallet= AepsWalletActions(myActivity,userSession)
        getAccountList()

    }

    private fun getAccountList() {
        binding.imageView.visibility=View.VISIBLE
        binding.llNoData.visibility=View.GONE
        binding.rvAccountList.visibility=View.GONE
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getPayoutBankList(requireContext(),token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
               val response:PayoutBankListResponse=Gson().fromJson(message,PayoutBankListResponse::class.java)
                if (response.error==false){
                    if (response.data?.isNotEmpty() == true){
                        accountList= mutableListOf()
                        accountList.addAll(response.data)

                        val accountListAdapter=PayoutAccountAdapter(myActivity,accountList,this@PayoutAccountList,this@PayoutAccountList)
                        binding.rvAccountList.adapter=accountListAdapter
                        binding.rvAccountList.layoutManager=LinearLayoutManager(myActivity)


                        binding.imageView.visibility=View.GONE
                        binding.llNoData.visibility=View.GONE
                        binding.rvAccountList.visibility=View.VISIBLE
                    }else{
                        binding.imageView.visibility=View.GONE
                        binding.llNoData.visibility=View.VISIBLE
                        binding.rvAccountList.visibility=View.GONE
                    }

                }else{
                    binding.imageView.visibility=View.GONE
                    binding.llNoData.visibility=View.VISIBLE
                    binding.rvAccountList.visibility=View.GONE
                }
            }

            override fun fail(from: String) {
                binding.imageView.visibility=View.GONE
                binding.llNoData.visibility=View.VISIBLE
                binding.rvAccountList.visibility=View.GONE
            }
        })
        
    }

    private fun nullActivityCheck() {
        if (activity !=null){
            myActivity= activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        binding.ivMenu.setOnClickListener {
           // findNavController().navigate(R.id.action_payoutAccountList_to_fragemntNetwork)
            findNavController().navigate(R.id.action_payoutAccountList_to_addPayoutAccount)
        }
    }


    override fun setData() {

    }

    override fun onItemCLicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        wallet.openTransferToWalletDialog("bank",model[pos]._id)
    }


    private fun deleteAccount(data: Data) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.deletePayoutAccount(requireContext(),token,data._id.toString(),object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:DeletePayoutBankAccountResponse=Gson().fromJson(message,DeletePayoutBankAccountResponse::class.java)
                if (!response.error){
                    showToast(requireContext(),response.message)
                    getAccountList()
                }else{
                    showToast(requireContext(),response.message)
                }

            }

            override fun fail(from: String) {
                showToast(requireContext(),from)
            }
        })
    }


    override fun onLongPressed(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete))
            .setTitle("Delete Account !")
            .setPositiveButton("YES") { dialog, which ->
                deleteAccount(model[pos])
                getAccountList()
            }
            .setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }.show()
    }


}