package app.pay.panda.fragments.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.ContextCompat
import app.pay.panda.R
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import app.pay.panda.BaseFragment
import app.pay.panda.activity.IntroActivity
import app.pay.panda.databinding.FragmentOnboardingStatusBinding
import app.pay.panda.fragments.DialogKYCInstruction
import app.pay.panda.fragments.PrivacyPolicy
import app.pay.panda.fragments.TermsAndConditions
import app.pay.panda.helperclasses.CommonClass
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.responsemodels.dashBoardData.DashBoardData
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class OnboardingStatusFragment : BaseFragment<FragmentOnboardingStatusBinding>(FragmentOnboardingStatusBinding::inflate) {
    private lateinit var myActivity: FragmentActivity
    private lateinit var userSession: UserSession
    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        getDashBoardData()
        setOnBoardingScreen()
    }
    private fun getDashBoardData() {
        val token=userSession.getData(Constant.USER_TOKEN).toString()

        UtilMethods.dashBoardData(myActivity,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response: DashBoardData = Gson().fromJson(message, DashBoardData::class.java)
                response.data?.let { userSession.setUserData(it) }
            }

            override fun fail(from: String) {
            }
        })
    }

    private fun setOnBoardingScreen() {

        when (userSession.getIntData(Constant.LOGIN_STEPS)) {
            1 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
            }

            2 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))

            }

            3 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvGstDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))

            }

            4 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvGstDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvBankDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))

            }

            5 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvGstDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvBankDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvUploadDocs.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))

            }

            6 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvGstDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvBankDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvUploadDocs.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvVideoKyc.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))

            }

            7 -> {
                binding.mcvVerifyId.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvPersonalDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvGstDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvBankDetails.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvUploadDocs.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                binding.mcvVideoKyc.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.bggrey))
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_pendingApprovalFragment)

            }

            else -> {


            }
        }
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.tvTnc.setOnClickListener {
            val tncFragment= TermsAndConditions()
            tncFragment.show(myActivity.supportFragmentManager,"TAG")
        }
        binding.tvPrivacy.setOnClickListener {
            val policy= PrivacyPolicy()
            policy.show(myActivity.supportFragmentManager,"TAG")
        }

        binding.mcvVerifyId.setOnClickListener {
            onStepButtonClick(1,requireContext());
        }
        binding.mcvBankDetails.setOnClickListener {
            onStepButtonClick(4,requireContext())
        }
        binding.mcvGstDetails.setOnClickListener {
            onStepButtonClick(3,requireContext())
        }
        binding.mcvPersonalDetails.setOnClickListener {
            onStepButtonClick(2,requireContext())
        }

        binding.mcvUploadDocs.setOnClickListener {
            onStepButtonClick(5,requireContext())
        }
        binding.mcvVideoKyc.setOnClickListener {
        /*    val dialogKYCInstruction = DialogKYCInstruction()
            dialogKYCInstruction.show(myActivity.supportFragmentManager, "KYCInstructionDialog")*/
           // onStepButtonClick(6,requireContext())

            val clickedStep = 6
            val loginSteps = userSession.getIntData(Constant.LOGIN_STEPS)

            // Check if the steps are completed
            when {
                clickedStep <= loginSteps -> Toast.makeText(requireContext(), "This step is already completed.", Toast.LENGTH_SHORT).show()
                clickedStep > loginSteps + 1 -> {
                    val previousStepName = getStepName(loginSteps)
                    Toast.makeText(requireContext(), "Please complete $previousStepName step.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Show the dialog if the previous steps are completed
                    val dialogKYCInstruction = DialogKYCInstruction()
                    dialogKYCInstruction.show(myActivity.supportFragmentManager, "KYCInstructionDialog")
                    navigateToStep(clickedStep)
                }
            }
        }
        binding.chkTnc.setOnCheckedChangeListener { _, _ ->
            if (userSession.getIntData(Constant.LOGIN_STEPS) >= 6) {
                binding.btnSubmit.background = ContextCompat.getDrawable(myActivity, R.drawable.submitt_btn)
            } else {
                binding.chkTnc.isChecked = false
                Toast.makeText(requireContext(), "First Complete All KYC Steps", Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnSubmit.setOnClickListener {
            if (binding.chkTnc.isChecked) {
                val token=userSession.getData(Constant.USER_TOKEN).toString()
                val requestData= hashMapOf<String,Any>()
                requestData["user_id"]=token
                UtilMethods.selfDeclaration(requireContext(),requestData,object:MCallBackResponse{
                    override fun success(from: String, message: String) {
                        userSession.setBoolData(Constant.ISSELFDECLARE,true)
                        userSession.setIntData(Constant.LOGIN_STEPS,7)
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "SUCCESS",
                            "All Login Steps are successfully completed. Now Your account will be reviewed by ADMIN","success",object:MyClick{
                                override fun onClick() {
                                    findNavController().navigate(R.id.action_onboardingStatusFragment_to_pendingApprovalFragment)
                                }
                            })
                    }

                    override fun fail(from: String) {
                        ShowDialog.bottomDialogSingleButton(myActivity,
                            "ERROR",
                            "Error While submitting Self Declaration. try After Some time","success",object:MyClick{
                                override fun onClick() {

                                }
                            })
                    }
                })

            } else {
                Toast.makeText(requireContext(), "Please Accept Terms and Conditions", Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun setData() {
        setOnBoardingScreen()
    }

    override fun onResume() {
        setOnBoardingScreen()
        super.onResume()
    }

    public fun onStepButtonClick(clickedStep: Int, context: Context) {
        val loginSteps=userSession.getIntData(Constant.LOGIN_STEPS)
        when {
            clickedStep <= loginSteps -> Toast.makeText(context, "This step is already completed.", Toast.LENGTH_SHORT).show()
            clickedStep > loginSteps + 1 -> {
                val previousStepName = getStepName(loginSteps)
                Toast.makeText(context, "Please complete $previousStepName step.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                navigateToStep(clickedStep)
            }
        }
    }

    private fun navigateToStep(clickedStep: Int) {
        when(clickedStep){
            1->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_verifyIdentityFragment)
            }
            4->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_onboardBankDetailsFragment)
            }
            3->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_businessDetailsFragment)
            }
            2->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_personalDetailsFragment)
            }
            5->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_uploadDocsFragment)
            }
            6->{
                findNavController().navigate(R.id.action_onboardingStatusFragment_to_videoKycFragment)
            }

        }

    }

    private fun getStepName(step: Int): String {
        return when (step) {
            0 -> "Identity Verification"
            1 -> "Personal Details"
            2 -> "GST Information"
            3 -> "Bank Details"
            4 -> "Document Upload"
            5 -> "KYC Video"
            6 -> "Self Declaration"
            else -> "Unknown Step"
        }
    }
    private fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}