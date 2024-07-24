package app.pay.panda.fragments.aepsFragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.AepsAllActions
import app.pay.panda.activity.AepsOnBoardingActivity
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.ScannerListAdapter
import app.pay.panda.databinding.DialogScannerDevicesBinding
import app.pay.panda.databinding.FragmentDailyAuthBinding
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.FingerPrintScanner
import app.pay.panda.helperclasses.ScanFinger
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.OnClick
import app.pay.panda.interfaces.ScannerListClick
import app.pay.panda.responsemodels.aepsDailyAuth.DailyAuthResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import org.json.JSONException


class FragmentDailyAuth : BaseFragment<FragmentDailyAuthBinding>(FragmentDailyAuthBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var selectedPackage = ""
    private var fData = ""
    private lateinit var scanFinger: ScanFinger
    private val startForScannerResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK && data != null) {
            fData = data.getStringExtra("PID_DATA").toString()
            if (fData.isNotEmpty()) {
                scanFinger.validateFingerPrint(fData, object : OnClick {
                    override fun onButtonClick() {
                        dailyAuth(fData)
                    }
                })

            } else {
                Toast.makeText(requireContext(), "No FingerPrint Data Found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Unable to Capture FingerPrint Data", Toast.LENGTH_SHORT).show()
        }

    }

    private fun dailyAuth(fData: String) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        val data = CommonClass.base64urlEncode(fData)
        requestData["accessmodetype"] = "APP"
        requestData["latitude"] = userSession.getData(Constant.M_LAT).toString()
        requestData["longitude"] = userSession.getData(Constant.M_LONG).toString()
        requestData["data"] = data
        requestData["user_id"] = token
        UtilMethods.aepsDailyAuth(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DailyAuthResponse = Gson().fromJson(message, DailyAuthResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity, "SUCCESS", response.message, "success", object : MyClick {
                        override fun onClick() {
                            startActivity(Intent(activity, AepsAllActions::class.java).putExtra("status", "4"))
                            activity?.overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
                            myActivity.finish()
                        }
                    })
                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", response.message, "error", object : MyClick {
                        override fun onClick() {

                        }
                    })
                }

            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", "Unable to Complete Authentication", "error", object : MyClick {
                    override fun onClick() {

                    }
                })
            }
        })

    }

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        scanFinger = ScanFinger(myActivity, userSession, startForScannerResult)
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
            val builder = AlertDialog.Builder(myActivity)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { _, _ ->
                    startActivity(Intent(myActivity, DashBoardActivity::class.java))
                    myActivity.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }

        binding.lytScanner.setEndIconOnClickListener {
            openDeviceDialog()
        }
        binding.atvScanner.setOnClickListener {
            openDeviceDialog()
        }
        binding.btnScanFinger.setOnClickListener {
            if (binding.atvScanner.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Select Scanner Device First", Toast.LENGTH_SHORT).show()
            } else {
                scanFinger.yourDevicePackage(selectedPackage)
            }
        }

    }

    private fun openDeviceDialog() {
        val scannerDialog: Dialog = Dialog(myActivity)
        val dBinding = DialogScannerDevicesBinding.inflate(myActivity.layoutInflater)
        dBinding.root.background =
            ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)
        scannerDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        scannerDialog.setContentView(dBinding.root)
        scannerDialog.window
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        scannerDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        scannerDialog.window?.attributes?.windowAnimations ?: R.style.DialogAnimationBottom
        scannerDialog.window?.setGravity(Gravity.BOTTOM)
        val list = FingerPrintScanner.getScannerList()
        val clickListener = object : ScannerListClick {
            override fun onItemClicked(
                holder: RecyclerView.ViewHolder,
                model: List<FingerPrintScanner>,
                pos: Int
            ) {
                userSession.setData(Constant.DEVICE_NAME, model[pos].getDeviceName())
                userSession.setData(Constant.SCANNER_PACKAGE, model[pos].getPackageName())
                binding.atvScanner.setText(model[pos].getDeviceName())
                selectedPackage = model[pos].getPackageName()
                scannerDialog.dismiss()
            }
        }
        val adapter = ScannerListAdapter(myActivity, list, clickListener)
        dBinding.rvScanner.adapter = adapter
        dBinding.rvScanner.layoutManager = LinearLayoutManager(myActivity)

        scannerDialog.setCancelable(false)
        scannerDialog.show()

    }

    override fun setData() {
        if (userSession.getData(Constant.DEVICE_NAME) != null) {
            binding.atvScanner.setText(userSession.getData(Constant.DEVICE_NAME))
        }
        if (userSession.getData(Constant.SCANNER_PACKAGE) != null) {
            selectedPackage = userSession.getData(Constant.SCANNER_PACKAGE).toString()
        }
    }

}