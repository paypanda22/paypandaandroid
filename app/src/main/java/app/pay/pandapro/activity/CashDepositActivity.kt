package app.pay.pandapro.activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.pay.pandapro.BaseActivity
import app.pay.pandapro.R
import app.pay.pandapro.databinding.ActivityCashDepositBinding
import app.pay.pandapro.fragments.aepsFragments.AepsBe
import app.pay.pandapro.fragments.aepsFragments.AepsCashDeposit
import app.pay.pandapro.fragments.aepsFragments.AepsCw
import app.pay.pandapro.fragments.aepsFragments.AepsMs
import app.pay.pandapro.fragments.aepsFragments.AepsTransactionList
import app.pay.pandapro.helperclasses.UserSession
import com.google.android.material.card.MaterialCardView

class CashDepositActivity : BaseActivity<ActivityCashDepositBinding>() {
    private lateinit var userSession: UserSession
    private val aepsBe = AepsBe()
    private val aepsCw = AepsCw<Any>()
    private val aepsMs = AepsMs()
    private val cashDeposit = AepsCashDeposit()
    private lateinit var active: Fragment
    private val aepsTxn = AepsTransactionList()
    private val container = R.id.fragmentContainer
    private var status = 0
    var catId=""
    var title=""
    var bank="aeps2"
    private var selectedAepsType: String = ""
    override fun getViewBinding(): ActivityCashDepositBinding = ActivityCashDepositBinding.inflate(layoutInflater)

    override fun init(savedInstanceState: Bundle?) {
        userSession = UserSession(this@CashDepositActivity)
        status = intent.getStringExtra("status").toString().toInt()
        title = intent.getStringExtra("title").toString()
        catId = intent.getStringExtra("catId").toString()
        selectedAepsType = intent.getStringExtra("selectedAepsType").toString()






        if(selectedAepsType== "Aeps 4"){
            bank="aeps4"
            binding.llCd.visibility=View.GONE
        }else {
            bank="aeps2"
        }

        loadFragment(status,catId,title,selectedAepsType)
        makeActive()
    }

    private fun makeActive() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
       if (currentFragment == aepsBe){
           markActive(binding.iv1, binding.tv1, binding.mcv1)
           markInActive(binding.iv3, binding.tv3, binding.mcv3)
           markInActive(binding.iv4, binding.tv4, binding.mcv4)
           markInActive(binding.iv5, binding.tv5, binding.mcv5)
       }else if (currentFragment== aepsMs){
           markInActive(binding.iv1, binding.tv1, binding.mcv1)
           markActive(binding.iv3, binding.tv3, binding.mcv3)
           markInActive(binding.iv4, binding.tv4, binding.mcv4)
           markInActive(binding.iv5, binding.tv5, binding.mcv5)
       }else if (currentFragment==cashDeposit){
           markInActive(binding.iv1, binding.tv1, binding.mcv1)
           markInActive(binding.iv3, binding.tv3, binding.mcv3)
           markActive(binding.iv4, binding.tv4, binding.mcv4)
           markInActive(binding.iv5, binding.tv5, binding.mcv5)
       }else if (currentFragment==aepsTxn){
           markInActive(binding.iv1, binding.tv1, binding.mcv1)
           markInActive(binding.iv3, binding.tv3, binding.mcv3)
           markInActive(binding.iv4, binding.tv4, binding.mcv4)
           markActive(binding.iv5, binding.tv5, binding.mcv5)
       }else{
           markInActive(binding.iv1, binding.tv1, binding.mcv1)
           markInActive(binding.iv3, binding.tv3, binding.mcv3)
           markActive(binding.iv4, binding.tv4, binding.mcv4)
           markInActive(binding.iv5, binding.tv5, binding.mcv5)
       }
    }
    private fun loadFragment(status: Int,catId:String,title:String,selectedAepsType:String) {
        when (status) {
            5 -> {
                active = aepsBe
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsBe.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsBe).commit()
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            6 -> {
                active = aepsMs
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsMs.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsMs).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            4 -> {
                active = cashDeposit
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                cashDeposit.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, cashDeposit).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            8 -> {
                active = aepsTxn
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsTxn.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsTxn).commit()
                markInActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markActive(binding.iv5, binding.tv5, binding.mcv5)
            }

            else -> {
                active = aepsBe
                val bundle = Bundle()

                bundle.putString("catId", catId.toString())
                bundle.putString("title", title.toString())
                bundle.putString("selectedAepsType", selectedAepsType.toString())
                aepsBe.arguments=bundle
                supportFragmentManager.beginTransaction().replace(container, aepsBe).commit()
                markActive(binding.iv1, binding.tv1, binding.mcv1)
                markInActive(binding.iv3, binding.tv3, binding.mcv3)
                markInActive(binding.iv4, binding.tv4, binding.mcv4)
                markInActive(binding.iv5, binding.tv5, binding.mcv5)
            }
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            val builder = AlertDialog.Builder(this@CashDepositActivity)
            builder.setMessage("Go to DashBoard ?")
                .setTitle("Exit !")
                .setNegativeButton("Exit") { dialog, which ->
                    startActivity(Intent(this@CashDepositActivity, DashBoardActivity::class.java))
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
                .setPositiveButton("No") { dialog, which ->
                    dialog.dismiss()
                }.show()
        }
        binding.llBe.setOnClickListener { loadFragment(5,catId,title,selectedAepsType) }
        binding.llMs.setOnClickListener { loadFragment(6,catId,title,selectedAepsType) }
        binding.llCd.setOnClickListener { loadFragment(4,catId,title,selectedAepsType) }
        binding.llTxn.setOnClickListener { loadFragment(8,catId,title,selectedAepsType) }
    }

    override fun setData() {

    }

    override fun handleBackPressCustom(): Boolean {
        val builder = AlertDialog.Builder(this@CashDepositActivity)
        builder.setMessage("Go to DashBoard ?")
            .setTitle("Exit !")
            .setNegativeButton("Exit") { dialog, which ->
                startActivity(Intent(this@CashDepositActivity, DashBoardActivity::class.java))
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
        mcv.setCardBackgroundColor(ContextCompat.getColor(this@CashDepositActivity, R.color.colorPrimaryDark))
        iv.setColorFilter(ContextCompat.getColor(this@CashDepositActivity, R.color.white))
        tv.setTextColor(ContextCompat.getColor(this@CashDepositActivity, R.color.colorPrimaryDark))
    }

    private fun markInActive(iv: ImageView, tv: TextView, mcv: MaterialCardView) {
        mcv.setCardBackgroundColor(ContextCompat.getColor(this@CashDepositActivity, R.color.white))
        iv.setColorFilter(ContextCompat.getColor(this@CashDepositActivity, R.color.black))
        tv.setTextColor(ContextCompat.getColor(this@CashDepositActivity, R.color.black))
    }

}