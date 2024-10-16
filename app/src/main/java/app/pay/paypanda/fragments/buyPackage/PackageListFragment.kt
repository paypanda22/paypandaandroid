package app.pay.paypanda.fragments.buyPackage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.BaseFragment
import app.pay.paypanda.R
import app.pay.paypanda.activity.DashBoardActivity
import app.pay.paypanda.activity.IntroActivity
import app.pay.paypanda.adapters.PackageListAdapter
import app.pay.paypanda.adapters.PackagePriceAdapter
import app.pay.paypanda.databinding.FragmentPackageListBinding
import app.pay.paypanda.databinding.LytDialogPackagePricingBinding
import app.pay.paypanda.helperclasses.ShowDialog
import app.pay.paypanda.helperclasses.UserSession
import app.pay.paypanda.helperclasses.Utils.Companion.showToast
import app.pay.paypanda.interfaces.MCallBackResponse
import app.pay.paypanda.interfaces.MyClick
import app.pay.paypanda.interfaces.PackageListClickListener
import app.pay.paypanda.interfaces.PackagePriceClick
import app.pay.paypanda.responsemodels.PackagePrizeDataDeserializer
import app.pay.paypanda.responsemodels.PostApiResponse
import app.pay.paypanda.responsemodels.packageListResponse.Data
import app.pay.paypanda.responsemodels.packageListResponse.PackageListResponse
import app.pay.paypanda.responsemodels.packageListResponse.Price
import app.pay.paypanda.responsemodels.realvalueresponse.RealValueResponse
import app.pay.paypanda.retrofit.Constant
import app.pay.paypanda.retrofit.UtilMethods
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder


class PackageListFragment : BaseFragment<FragmentPackageListBinding>(FragmentPackageListBinding::inflate), PackageListClickListener,
    PackagePriceClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var packageList: MutableList<Data>
    private lateinit var buyNowDialog: Dialog
    private lateinit var dBinding: LytDialogPackagePricingBinding
    private var package_id = ""
    private var package_price = "0"
    private var walletAmt = "0"
    private var termcon=""
    var real_prize=""
    private lateinit var txnDetailList:MutableList<app.pay.paypanda.responsemodels.packagedetail.Data>
    private lateinit var txnDetail:MutableList<app.pay.paypanda.responsemodels.realvalueresponse.Price>
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getPackageList()


    }

    private fun getPackageList() {
        binding.llNoData.visibility = GONE
        binding.rvPackageList.visibility = GONE
        binding.imageView.visibility = VISIBLE
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getPackageList(requireContext(), token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: PackageListResponse = Gson().fromJson(message, PackageListResponse::class.java)
                if (response.error == false) {
                    if (response.data?.isNotEmpty() == true) {
                        packageList = mutableListOf()
                        packageList.addAll(response.data)

                        val packageAdapter = PackageListAdapter(myActivity, packageList, this@PackageListFragment)
                        binding.rvPackageList.adapter = packageAdapter
                        binding.rvPackageList.layoutManager = LinearLayoutManager(myActivity)

                        binding.llNoData.visibility = GONE
                        binding.rvPackageList.visibility = VISIBLE
                        binding.imageView.visibility = GONE
                    } else {
                        binding.llNoData.visibility = VISIBLE
                        binding.rvPackageList.visibility = GONE
                        binding.imageView.visibility = GONE
                    }

                } else {
                    binding.llNoData.visibility = VISIBLE
                    binding.rvPackageList.visibility = GONE
                    binding.imageView.visibility = GONE
                }
            }

            override fun fail(from: String) {
                binding.llNoData.visibility = VISIBLE
                binding.rvPackageList.visibility = GONE
                binding.imageView.visibility = GONE
            }
        })
    }

    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(myActivity, DashBoardActivity::class.java))
            myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }

    }

    override fun setData() {

    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        package_id = model[pos]._id.toString()
        packageDatails(package_id)
        termcon=model[pos].isPaid.toString()
        model[pos].prices?.let { openBuyDialog(model[pos].package_name, it) }
    }

    override fun onItemClickedDetail(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        package_id = model[pos]._id.toString()
        val bundle = Bundle().apply {
            putString("id", package_id)
        }
        findNavController().navigate(R.id.fragment_package_list_to_fragment_package_detail,bundle)
    }

    private fun openBuyDialog(packageName: String?, prices: List<Price>) {
        buyNowDialog = Dialog(myActivity)
        dBinding = LytDialogPackagePricingBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        buyNowDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        buyNowDialog.setContentView(dBinding.root)
        buyNowDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        buyNowDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        buyNowDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        buyNowDialog.window?.setGravity(Gravity.BOTTOM)
        dBinding.tvPackageName.text = packageName.toString()
        val priceAdapter = PackagePriceAdapter(myActivity, prices, this@PackageListFragment)
        dBinding.rvPriceList.adapter = priceAdapter
        dBinding.rvPriceList.layoutManager = LinearLayoutManager(myActivity)
        dBinding.textViewTermsConditions.text = Html.fromHtml(
            "I agree to the <font color='#2e3192'>terms and condition</font>",
            Html.FROM_HTML_MODE_LEGACY
        )
        if(!termcon.equals("true")){
            dBinding.termcon.visibility= VISIBLE
        }else{
            dBinding.termcon.visibility= GONE
        }

        dBinding.textViewTermsConditions.setOnClickListener {
            val dialog = AlertDialog.Builder(myActivity)
           // dBinding.textViewTermsConditions.text = Html.fromHtml(getString(R.string.termsconditions))
            val dialogView = layoutInflater.inflate(R.layout.dialog_terms_conditions, null)
            dialog.setView(dialogView)
            dialog.setPositiveButton("OK", null)
            dialog.show()

        }

        dBinding.btnBuyNow.setOnClickListener {
            if (dBinding.chkRememberMe.isChecked.equals(true)) {

                if (dBinding.llPin.visibility != VISIBLE) {
                    showToast(requireContext(), "Select Package First")
                } else if (dBinding.edtTPin.text.toString().length < 4) {
                    showToast(requireContext(), "Enter Transaction Pin")
                } else {
                    val token = userSession.getData(Constant.USER_TOKEN).toString()
                    val requestData = hashMapOf<String, Any?>()
                    requestData["package_id"] = package_id
                    requestData["tpin"] = dBinding.edtTPin.text.toString()
                    requestData["price"] = package_price
                    requestData["user_id"] = token
                    UtilMethods.purchasePackage(
                        requireContext(),
                        requestData,
                        object : MCallBackResponse {
                            override fun success(from: String, message: String) {
                                val response: PostApiResponse =
                                    Gson().fromJson(message, PostApiResponse::class.java)
                                if (!response.error) {
                                    ShowDialog.bottomDialogSingleButton(myActivity,
                                        "Congratulations!",
                                        response.message,
                                        "success",
                                        object : MyClick {
                                            override fun onClick() {
                                                findNavController().popBackStack()
                                                buyNowDialog.dismiss()
                                            }
                                        })

                                } else {
                                    Toast.makeText(activity, response.message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }

                            override fun fail(from: String) {
                                Toast.makeText(activity, from, Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            } else {
                Toast.makeText(activity, "Please Apply Terms and Conditions", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        buyNowDialog.setCancelable(true)
        buyNowDialog.show()
    }
    private fun packageDatails(package_id: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.packageDatails(requireContext(), package_id, token, object : MCallBackResponse {
            @SuppressLint("SetTextI18n")
            override fun success(from: String, message: String) {
                try {
                    val gson = GsonBuilder()
                        .registerTypeAdapter(object : TypeToken<List<Data>>() {}.type, PackagePrizeDataDeserializer())
                        .create()
                    val response: RealValueResponse = gson.fromJson(message, RealValueResponse::class.java)

                    // Ensure data and prices are not null or empty
                    val prices = response.data.prices

                    if (!prices.isNullOrEmpty()) {
                        // Assign the prices list to txnDetail
                        txnDetail = prices.toMutableList()

                        // Get the real_value from the first price
                        real_prize = txnDetail[0].real_value.toString()
                        userSession = UserSession(requireContext())
                        dBinding.tvCurrentAmount.text = userSession.getData(Constant.MAIN_WALET).toString()
                        walletAmt= userSession.getData(Constant.MAIN_WALET).toString()
                        dBinding.totalamt.text= real_prize
                        val walletAmtDouble = walletAmt.toDoubleOrNull() ?: 0.0
                        val realPrizeDouble = real_prize.toDoubleOrNull() ?: 0.0
                        val available_bal = walletAmtDouble - realPrizeDouble
// Format the result if necessary


                        dBinding.tvAvailableAmount.text=available_bal.toString()
                        // Log or handle the real prize value
                        Log.d("PackageDetails", "Real Value: $real_prize")

                        // Perform any additional logic if necessary
                        // You can display or assign the realValue as needed
                    } else {
                        Log.e("PackageDetails", "Prices list is empty or null")
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast(requireContext(), "Error parsing response: ${e.message}")
                }
            }
            override fun fail(from: String) {
                showToast(requireContext(), "Error: $from")
            }
        })
    }





    override fun onPriceSelected(holder: RecyclerView.ViewHolder, model: List<Price>, pos: Int) {
        package_price = model[pos]._id.toString()
        dBinding.llPin.visibility= VISIBLE
    }

}