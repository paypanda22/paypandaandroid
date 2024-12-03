package app.pay.retailers.fragments.payout

import android.app.AlertDialog
import android.content.Intent
import android.view.View
import androidx.activity.addCallback

import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R

import app.pay.retailers.activity.DashBoardActivity
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.PayoutAccountAdapter
import app.pay.retailers.databinding.FragmentPayoutAccountListBinding
import app.pay.retailers.helperclasses.AepsWalletActions
import app.pay.retailers.helperclasses.ShowDialog
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.helperclasses.Utils.Companion.showToast
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.MyClick
import app.pay.retailers.interfaces.UserBankAccountClick
import app.pay.retailers.responsemodels.deletePayoutBank.DeletePayoutBankAccountResponse
import app.pay.retailers.responsemodels.payoutBanks.Data
import app.pay.retailers.responsemodels.payoutBanks.PayoutBankListResponse
import app.pay.retailers.responsemodels.payoutenquiry.PayoutEnquiryResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


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
       // navigateBack()
    }

    private fun getAccountList() {
        binding.imageView.visibility=View.VISIBLE
        binding.llNoData.visibility=View.GONE
        binding.rvAccountList.visibility=View.GONE
        binding.swipeRefreshLayout.isRefreshing = true
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getPayoutBankList(requireContext(),token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
               val response:PayoutBankListResponse=Gson().fromJson(message,PayoutBankListResponse::class.java)
                if (response.error==false){
                    if (response.data?.isNotEmpty() == true){
                        accountList= mutableListOf()
                        accountList.addAll(response.data)
                        binding.swipeRefreshLayout.isRefreshing = false
                        val accountListAdapter=PayoutAccountAdapter(myActivity,accountList,this@PayoutAccountList,this@PayoutAccountList)
                        binding.rvAccountList.adapter=accountListAdapter
                        binding.rvAccountList.layoutManager=LinearLayoutManager(myActivity)


                        binding.imageView.visibility=View.GONE
                        binding.llNoData.visibility=View.GONE
                        binding.rvAccountList.visibility=View.VISIBLE
                    }else{
                       // findNavController().navigate(R.id.action_payoutAccountList_to_addPayoutAccount)
                        binding.swipeRefreshLayout.isRefreshing = false
                        binding.imageView.visibility=View.GONE
                        binding.llNoData.visibility=View.VISIBLE
                        binding.rvAccountList.visibility=View.GONE
                    }

                }else{
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.imageView.visibility=View.GONE
                    binding.llNoData.visibility=View.VISIBLE
                    binding.rvAccountList.visibility=View.GONE
                }
            }

            override fun fail(from: String) {
                binding.swipeRefreshLayout.isRefreshing = false
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

        binding.ivMenu.setOnClickListener {
           // findNavController().navigate(R.id.action_payoutAccountList_to_fragemntNetwork)
            findNavController().navigate(R.id.action_payoutAccountList_to_addPayoutAccount)
        }
        binding.ivBack.setOnClickListener{
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
            requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)


        }
        binding.swipeRefreshLayout.setOnRefreshListener{
            getAccountList()
        }
    }


    override fun setData() {

    }

    override fun onItemCLicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        wallet.openTransferToWalletDialog("bank",model[pos]._id,model[pos].bank_ifsc,model[pos].bank_account_name,model[pos].bank_name,model[pos].bank_account_number.toString(),model[pos].mobile_number.toString())
    }

    override fun onItemCLickeddelete(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete))
            .setTitle("Delete Account !")
            .setPositiveButton("YES") { dialog, which ->
                deleteAccount(model[pos])
            }
            .setNegativeButton("NO") { dialog, which ->
                dialog.dismiss()
            }.show()
        }

    override fun onItemCLickedstatus(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        payoutenquiry(model[pos])
    }


    private fun deleteAccount(data: Data) {
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.deletePayoutAccount(requireContext(),token,data._id.toString(),object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:DeletePayoutBankAccountResponse=Gson().fromJson(message,DeletePayoutBankAccountResponse::class.java)
                if (!response.error){
                    ShowDialog.bottomDialogSingleButton(myActivity, "Success",
                        response.message, "", object : MyClick {
                            override fun onClick() {
                                //dmtDialog.dismiss()
                                getAccountList()
                            }
                        })



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

    }
    private fun payoutenquiry(data: Data) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.payoutenquiry(
            requireContext(),
            data._id.toString(),
            token,
            object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: PayoutEnquiryResponse =
                        Gson().fromJson(message, PayoutEnquiryResponse::class.java)
                    if (!response.error) {
                       showToast(requireContext(), response.message)
                      //  CommonToast.show(myActivity, response.message,R.drawable.horizontal_logo)
                    } else {
                        showToast(myActivity, response.message)
                    }

                }

                override fun fail(from: String) {
                    showToast(myActivity, from)
                }
            })
    }
   /* private fun handleBackPress() {
        // Set the callback for back press
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startActivity(Intent(requireContext(), DashBoardActivity::class.java))
                    requireActivity().overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

                }
            })
    }*/

    private fun navigateBack() {
        // Finish the current activity to navigate back to the Dashboard
        startActivity(Intent(myActivity, DashBoardActivity::class.java))
    }

    override fun onResume() {
        super.onResume()
        // Handle back press to finish the activity
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateBack()
        }
    }
}