package app.pay.retailers.fragments.recharge

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.retailers.BaseFragment
import app.pay.retailers.R
import app.pay.retailers.activity.IntroActivity
import app.pay.retailers.adapters.ROfferAdapter
import app.pay.retailers.databinding.FragmentFetchPlansBinding
import app.pay.retailers.helperclasses.UserSession
import app.pay.retailers.interfaces.CommonPlanData
import app.pay.retailers.interfaces.CommonPlansClick
import app.pay.retailers.interfaces.GeneralPlanAdapter
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.interfaces.ROfferClick
import app.pay.retailers.responsemodels.rechargePlans.Plans
import app.pay.retailers.responsemodels.rechargePlans.RechargePlansResponse
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson


class FetchPlansFragment : BaseFragment<FragmentFetchPlansBinding>(FragmentFetchPlansBinding::inflate),ROfferClick ,CommonPlansClick{
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var rOffer:MutableList<Plans?>
    private lateinit var dataPlans:MutableList<CommonPlanData>
    private lateinit var frcPlans:MutableList<CommonPlanData>
    private lateinit var fullTalktime:MutableList<CommonPlanData>
    private lateinit var roamingPlans:MutableList<CommonPlanData>
    private lateinit var topUpPlans:MutableList<CommonPlanData>
    private var mobile=""
    override fun init() {
       nullActivityCheck()
        userSession= UserSession(requireContext())
        mobile=arguments?.getString("mobile").toString()
        getMobilePlans()
    }

    private fun getMobilePlans() {
        binding.llNoData.visibility=View.GONE
        binding.rlMainContent.visibility= GONE
        binding.hSView.visibility= GONE
        binding.imageView.visibility= VISIBLE
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getRechargePlans(requireContext(),mobile,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:RechargePlansResponse=Gson().fromJson(message,RechargePlansResponse::class.java)
                if (!response.error){
                    dataPlans= mutableListOf()
                    dataPlans.addAll(response.data.offerPlan.DATA)
                    if (response.data.plans.isNullOrEmpty()){
                        binding.tvROffer.visibility=GONE
                        makeActive(binding.tvDataPlans,binding.tvROffer,binding.tvFrc,binding.tvRoaming,binding.tvTopUp,binding.tvFullTT)
                        setGeneralPlansAdapter(dataPlans)
                    }else{
                        rOffer= mutableListOf()
                        rOffer.addAll(response.data.plans)
                        setRofferAdapter()
                        binding.tvROffer.visibility= VISIBLE
                    }



                    frcPlans= mutableListOf()
                    frcPlans.addAll(response.data.offerPlan.FRC)

                    fullTalktime= mutableListOf()
                    fullTalktime.addAll(response.data.offerPlan.FULLTT)

                    roamingPlans= mutableListOf()
                    roamingPlans.addAll(response.data.offerPlan.Romaing)

                    topUpPlans= mutableListOf()
                    topUpPlans.addAll(response.data.offerPlan.TOPUP)



                    binding.llNoData.visibility=View.GONE
                    binding.rlMainContent.visibility= VISIBLE
                    binding.hSView.visibility= VISIBLE
                    binding.imageView.visibility= GONE
                }else{
                    binding.llNoData.visibility=View.VISIBLE
                    binding.rlMainContent.visibility= GONE
                    binding.hSView.visibility= GONE
                    binding.imageView.visibility= GONE
                }
            }

            override fun fail(from: String) {
                binding.llNoData.visibility=View.VISIBLE
                binding.rlMainContent.visibility= GONE
                binding.hSView.visibility= GONE
                binding.imageView.visibility= GONE
            }
        })
    }

    private fun setRofferAdapter() {
        makeActive(binding.tvROffer,binding.tvDataPlans,binding.tvFrc,binding.tvRoaming,binding.tvTopUp,binding.tvFullTT)
       if (rOffer.isEmpty()){
           binding.llNoData.visibility=View.VISIBLE
           binding.rlMainContent.visibility= GONE
           binding.hSView.visibility= VISIBLE
           binding.imageView.visibility= GONE
       }else{
           val rofferAdapter= ROfferAdapter(myActivity,rOffer,this@FetchPlansFragment)
           binding.rvPlans.adapter=rofferAdapter
           binding.rvPlans.layoutManager=LinearLayoutManager(myActivity)

           binding.llNoData.visibility=View.GONE
           binding.rlMainContent.visibility= VISIBLE
           binding.hSView.visibility= VISIBLE
           binding.imageView.visibility= GONE
       }
    }

    private fun nullActivityCheck() {
        if (activity !=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvROffer.setOnClickListener { setRofferAdapter() }
        binding.tvDataPlans.setOnClickListener {
            makeActive(binding.tvDataPlans,binding.tvROffer,binding.tvFrc,binding.tvRoaming,binding.tvTopUp,binding.tvFullTT)
            setGeneralPlansAdapter(dataPlans) }
        binding.tvFrc.setOnClickListener {
            makeActive(binding.tvFrc,binding.tvDataPlans,binding.tvROffer,binding.tvRoaming,binding.tvTopUp,binding.tvFullTT)
            setGeneralPlansAdapter(frcPlans)
        }
        binding.tvRoaming.setOnClickListener {
            makeActive(binding.tvRoaming,binding.tvFrc,binding.tvDataPlans,binding.tvROffer,binding.tvTopUp,binding.tvFullTT)
            setGeneralPlansAdapter(roamingPlans)
        }
        binding.tvTopUp.setOnClickListener {
            makeActive(binding.tvTopUp,binding.tvRoaming,binding.tvFrc,binding.tvDataPlans,binding.tvROffer,binding.tvFullTT)
            setGeneralPlansAdapter(topUpPlans)
        }
        binding.tvFullTT.setOnClickListener {
            makeActive(binding.tvFullTT,binding.tvTopUp,binding.tvRoaming,binding.tvFrc,binding.tvDataPlans,binding.tvROffer)
            setGeneralPlansAdapter(fullTalktime)
        }

    }

    private fun setGeneralPlansAdapter(planList: MutableList<CommonPlanData>) {
        if (planList.isEmpty()){
            binding.llNoData.visibility=View.VISIBLE
            binding.rlMainContent.visibility= GONE
            binding.hSView.visibility= VISIBLE
            binding.imageView.visibility= GONE
        }else{
            val plansAdapter= GeneralPlanAdapter(myActivity,planList,this@FetchPlansFragment)
            binding.rvPlans.adapter=plansAdapter
            binding.rvPlans.layoutManager=LinearLayoutManager(myActivity)

            binding.llNoData.visibility=View.GONE
            binding.rlMainContent.visibility= VISIBLE
            binding.hSView.visibility= VISIBLE
            binding.imageView.visibility= GONE
        }
    }

    private fun makeActive(tv1:TextView,tv2:TextView,tv3:TextView,tv4:TextView,tv5:TextView,tv6:TextView){
        tv1.setBackgroundResource(R.drawable.btn_success)
        tv2.setBackgroundResource(R.drawable.btn_grey)
        tv3.setBackgroundResource(R.drawable.btn_grey)
        tv4.setBackgroundResource(R.drawable.btn_grey)
        tv5.setBackgroundResource(R.drawable.btn_grey)
        tv6.setBackgroundResource(R.drawable.btn_grey)
    }

    override fun setData() {

    }

    override fun onOfferItemClicked(holder: RecyclerView.ViewHolder, model: List<Plans?>, pos: Int) {
        val bundle = Bundle().apply {
            putString("amount", model[pos]?.price.toString())
        }
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        findNavController().popBackStack()
    }

    override fun onGeneralPlanSelected(holder: RecyclerView.ViewHolder, model: List<CommonPlanData>, pos: Int) {
        val bundle = Bundle().apply {
            putString("amount", model[pos].rs.toString())
        }
        parentFragmentManager.setFragmentResult("requestKey", bundle)
        findNavController().popBackStack()
    }

}