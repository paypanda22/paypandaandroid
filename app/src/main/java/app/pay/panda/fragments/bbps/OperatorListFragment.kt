package app.pay.panda.fragments.bbps

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.DashBoardActivity
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.OperatorListAdapter
import app.pay.panda.databinding.FragmentOperatorListBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.OplistClickListner
import app.pay.panda.responsemodels.getOperators.CustomerParam
import app.pay.panda.responsemodels.getOperators.Data
import app.pay.panda.responsemodels.getOperators.GetOperatorsResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson

class OperatorListFragment : BaseFragment<FragmentOperatorListBinding>(FragmentOperatorListBinding::inflate), OplistClickListner {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var service: String
    private lateinit var catID: String
    private lateinit var opList: MutableList<Data>
    private lateinit var opListAdapter: OperatorListAdapter

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        service = arguments?.getString("service").toString()
        catID = arguments?.getString("catID").toString()
        getOperatorList()
    }

    private fun getOperatorList() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getBbpsOperators(requireContext(), catID, token, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: GetOperatorsResponse = Gson().fromJson(message, GetOperatorsResponse::class.java)
                if (response.error) {
                    showNoDataLayout()
                } else {
                    if (response.data.isNotEmpty()) {
                        opList = mutableListOf()
                        opList.addAll(response.data)
                        setupRecyclerView()
                        showRecyclerView()
                    } else {
                        showNoDataLayout()
                    }
                }
            }

            override fun fail(from: String) {
                showNoDataLayout()
            }
        })
    }

    private fun showRecyclerView() {
        binding.rlNoData.visibility = GONE
        binding.rlImageView.visibility = GONE
        binding.recyclerview.visibility = VISIBLE
    }

    private fun showNoDataLayout() {
        binding.rlNoData.visibility = VISIBLE
        binding.recyclerview.visibility = GONE
        binding.rlImageView.visibility = GONE
    }

    private fun setupRecyclerView() {
        opListAdapter = OperatorListAdapter(myActivity, opList, this@OperatorListFragment)
        binding.recyclerview.adapter = opListAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(myActivity)
    }

    private fun nullActivityCheck() {
        if (activity == null) {
            startActivity(Intent(context, IntroActivity::class.java))
        } else {
            myActivity = activity as FragmentActivity
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(activity, DashBoardActivity::class.java))
            activity?.overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
        }
        binding.edtSearchBiller.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterResults(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterResults(query: String) {
        if (::opListAdapter.isInitialized) {
            opListAdapter.filter.filter(query)
            // Show "No Data" layout if no results match the search query
            if (opListAdapter.itemCount == 0) {
                showNoDataLayout()
            } else {
                showRecyclerView()
            }
        }
    }

    override fun setData() {
        binding.tvServiceName.text = service
    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Data>, pos: Int) {
        val bundle = Bundle().apply {
            val list: MutableList<CustomerParam> = mutableListOf()
            list.addAll(model[pos].customerParams)
            putParcelableArrayList("params", ArrayList(list))
            putString("catName", service)
            putString("name", model[pos].name)
            putString("operatorid", model[pos].operatorid)
            putString("paymentAmountExactness", model[pos].paymentAmountExactness)
            putString("fetchRequirement", model[pos].fetchRequirement)
            putString("catID", catID)
        }
        findNavController().navigate(R.id.action_operatorListFragment2_to_fetchBillPayment, bundle)
    }
}
