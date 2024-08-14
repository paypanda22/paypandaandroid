package app.pay.panda.fragments.buyPackage

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.PackageListAdapter
import app.pay.panda.adapters.PackagePriceAdapter
import app.pay.panda.adapters.ScannerListAdapter
import app.pay.panda.databinding.DialogScannerDevicesBinding
import app.pay.panda.databinding.FragmentPackageListBinding
import app.pay.panda.databinding.LytDialogPackagePricingBinding
import app.pay.panda.helperclasses.FingerPrintScanner
import app.pay.panda.helperclasses.MyGlide
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.PackageListClickListener
import app.pay.panda.interfaces.PackagePriceClick
import app.pay.panda.interfaces.ScannerListClick
import app.pay.panda.responsemodels.packageListResponse.Data
import app.pay.panda.responsemodels.packageListResponse.PackageListResponse
import app.pay.panda.responsemodels.packageListResponse.Price
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson


class PackageListFragment : BaseFragment<FragmentPackageListBinding>(FragmentPackageListBinding::inflate), PackageListClickListener,
    PackagePriceClick {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var packageList: MutableList<Data>
    private lateinit var buyNowDialog: Dialog
    private lateinit var dBinding: LytDialogPackagePricingBinding
    private var package_id = ""
    private var package_price = "0"

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
        model[pos].prices?.let { openBuyDialog(model[pos].package_name, it) }
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

        dBinding.btnBuyNow.setOnClickListener {
            if (dBinding.llPin.visibility!= VISIBLE){
                showToast(requireContext(),"Select Package First")
            }else if (dBinding.edtTPin.text.toString().length<4){
                showToast(requireContext(),"Enter Transaction Pin")
            }else{
                val token=userSession.getData(Constant.USER_TOKEN).toString()
                val requestData= hashMapOf<String,Any?>()
                requestData["package_id"]=package_id
                requestData["tpin"]=dBinding.edtTPin.text.toString()
                requestData["price"]=package_price
                requestData["user_id"]=token
                UtilMethods.purchasePackage(requireContext(),requestData,object:MCallBackResponse{
                    override fun success(from: String, message: String) {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "Congratulations!",
                            "Package buy Successfully",
                            "success",
                            object : MyClick {
                                override fun onClick() {
                                    findNavController().popBackStack()
                                    buyNowDialog.dismiss()
                                }
                            })

                    }

                    override fun fail(from: String) {

                    }
                })
            }
        }

        buyNowDialog.setCancelable(true)
        buyNowDialog.show()
    }

    override fun onPriceSelected(holder: RecyclerView.ViewHolder, model: List<Price>, pos: Int) {
        package_price = model[pos]._id.toString()
        dBinding.llPin.visibility= VISIBLE
    }

}