package app.pay.paypanda.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.pay.paypanda.BaseActivity
import app.pay.paypanda.R
import app.pay.paypanda.databinding.FragmentAepsAllActionsBinding
import app.pay.paypanda.fragments.aepsFragments.AadhaarPay
import app.pay.paypanda.fragments.aepsFragments.AepsBe
import app.pay.paypanda.fragments.aepsFragments.AepsCw
import app.pay.paypanda.fragments.aepsFragments.AepsMs
import app.pay.paypanda.fragments.aepsFragments.AepsTransactionList
import app.pay.paypanda.helperclasses.UserSession
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

    override fun getViewBinding(): FragmentAepsAllActionsBinding = FragmentAepsAllActionsBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@AepsAllActions)
        status = intent.getStringExtra("status").toString().toInt()
        serviceName = intent.getStringExtra("serviceName").toString()

        loadFragment(status)
        makeActive()
   if(serviceName == "Aeps Cash Deposit"){
       binding.llAp.visibility=View.GONE
   }else if(serviceName == "Aeps Adhaar pay"){
       binding.llCw.visibility=View.GONE
   }else if(serviceName == "Aeps Bank Withdraw"){
       binding.llAp.visibility=View.GONE
   }else{

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

    private fun loadFragment(status: Int) {
        when (status) {
            4 -> {
                active = aepsBe
                supportFragmentManager.beginTransaction().replace(container, aepsBe).commit()
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            5 -> {
                active = aepsCw
                supportFragmentManager.beginTransaction().replace(container, aepsCw).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            6 -> {
                active = aepsMs
                supportFragmentManager.beginTransaction().replace(container, aepsMs).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            7 -> {
                active = aadhaarPay
                supportFragmentManager.beginTransaction().replace(container, aadhaarPay).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            8 -> {
                active = aepsTxn
                supportFragmentManager.beginTransaction().replace(container, aepsTxn).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv2, binding.tv2, binding.mcv2)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            else -> {
                active = aepsBe
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
        binding.llBe.setOnClickListener { loadFragment(4) }
        binding.llCw.setOnClickListener { loadFragment(5) }
        binding.llMs.setOnClickListener { loadFragment(6) }
        binding.llAp.setOnClickListener { loadFragment(7) }
        binding.llTxn.setOnClickListener { loadFragment(8) }
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