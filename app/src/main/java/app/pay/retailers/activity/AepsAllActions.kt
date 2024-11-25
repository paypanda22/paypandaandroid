package app.pay.retailers.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.pay.retailers.BaseActivity
import app.pay.retailers.R
import app.pay.retailers.databinding.FragmentAepsAllActionsBinding
import app.pay.retailers.fragments.aepsFragments.AadhaarPay
import app.pay.retailers.fragments.aepsFragments.AepsBe
import app.pay.retailers.fragments.aepsFragments.AepsCw
import app.pay.retailers.fragments.aepsFragments.AepsMs
import app.pay.retailers.fragments.aepsFragments.AepsTransactionList
import app.pay.retailers.helperclasses.UserSession
import com.google.android.material.card.MaterialCardView


class AepsAllActions : BaseActivity<FragmentAepsAllActionsBinding>() {
    private lateinit var userSession: UserSession
    private val aepsBe = AepsBe()
    private val aepsCw = AepsCw<Any>()
    private val aepsMs = AepsMs()
    private val aadhaarPay = AadhaarPay()
    private lateinit var active: Fragment
    private val aepsTxn = AepsTransactionList()
    private val container = R.id.fragmentContainer
    private var status = 0
    private var serviceName = ""
    var catId=""
    var title=""
    var bank="aeps2"
    private var selectedAepsType: String = ""
    private val bundle = Bundle()
    override fun getViewBinding(): FragmentAepsAllActionsBinding = FragmentAepsAllActionsBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@AepsAllActions)
        status = intent.getStringExtra("status").toString().toInt()
        title = intent.getStringExtra("title").toString()
        catId = intent.getStringExtra("catId").toString()
        selectedAepsType = intent.getStringExtra("selectedAepsType").toString()




        loadFragment(status,catId,title,selectedAepsType)
        makeActive()
   if(title == "Aeps Cash Deposit"){
       binding.llAp.visibility=View.GONE

   }else if(title == "Aeps Adhaar pay"){
       binding.llCw.visibility=View.GONE

   }else if(title == "Aeps Bank Withdraw"){
       binding.llAp.visibility=View.GONE

   }else{

   }
        if(selectedAepsType== "Aeps 4"){
            bank="aeps4"
            binding.llAp.visibility=View.GONE
        }else {
            bank="aeps2"
        }
    }

    fun makeActive() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val name = currentFragment?.javaClass?.simpleName
        when (name) {
            "AepsBe" -> {
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            "AepsCw" -> {
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            "AepsMs" -> {
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            "AadhaarPay" -> {
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            "AepsTransactionList" -> {
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            else -> {
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }
        }
    }
 /*   private fun passArguments(fragment: Fragment, title: String, catId: String, selectedAepsType: String) {
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("catId", catId)
        bundle.putString("selectedAepsType", selectedAepsType)
        fragment.arguments = bundle
    }*/
    private fun loadFragment(status: Int,catId:String,title:String,selectedAepsType:String) {
        when (status) {
            4 -> {
                active = aepsBe
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsBe.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsBe).commit()
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            5 -> {
                active = aepsCw
                val bundle = Bundle()
                bundle.putString("status", status.toString())
                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsCw.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsCw).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            6 -> {
                active = aepsMs
                val bundle = Bundle()
                bundle.putString("status", status.toString())
                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsMs.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsMs).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            7 -> {
                active = aadhaarPay
                val bundle = Bundle()
                bundle.putString("status", status.toString())
                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aadhaarPay.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aadhaarPay).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            8 -> {
                active = aepsTxn
                val bundle = Bundle()
                bundle.putString("status", status.toString())
                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsTxn.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsTxn).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            else -> {
                active = aepsBe
                val bundle = Bundle()
                bundle.putString("status", status.toString())
                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsBe.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsBe).commit()
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }
        }
    }

    override fun addListeners() {

        binding.ivBack.setOnClickListener {
            val builder = AlertDialog.Builder(this@AepsAllActions)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { dialog, which ->
                    startActivity(Intent(this@AepsAllActions, DashBoardActivity::class.java))
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
        binding.llBe.setOnClickListener { loadFragment(4,catId,title,selectedAepsType) }
        binding.llCw.setOnClickListener { loadFragment(5,catId,title,selectedAepsType) }
        binding.llMs.setOnClickListener { loadFragment(6,catId,title,selectedAepsType) }
        binding.llAp.setOnClickListener { loadFragment(7,catId,title,selectedAepsType) }
        binding.llTxn.setOnClickListener { loadFragment(8,catId,title,selectedAepsType) }
    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val builder = AlertDialog.Builder(this@AepsAllActions)
        builder.setMessage("Go to DashBoard ?")
            .setTitle("Exit !")
            .setNegativeButton("Exit") { dialog, which ->
                startActivity(Intent(this@AepsAllActions, DashBoardActivity::class.java))
                overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
            }
            .setPositiveButton("No") { dialog, which ->
                dialog.dismiss()
            }.show()
        return true
    }

    override fun onClick(v: View?) {

    }

    private fun markActive(iv: ImageView, tv: TextView, mcv: MaterialCardView) {
        mcv.setCardBackgroundColor(ContextCompat.getColor(this@AepsAllActions, R.color.colorPrimaryDark))
        iv.setColorFilter(ContextCompat.getColor(this@AepsAllActions, R.color.white))
        tv.setTextColor(ContextCompat.getColor(this@AepsAllActions, R.color.colorPrimaryDark))
    }

    private fun markInActive(iv: ImageView, tv: TextView, mcv: MaterialCardView) {
        mcv.setCardBackgroundColor(ContextCompat.getColor(this@AepsAllActions, R.color.white))
        iv.setColorFilter(ContextCompat.getColor(this@AepsAllActions, R.color.black))
        tv.setTextColor(ContextCompat.getColor(this@AepsAllActions, R.color.black))
    }


}