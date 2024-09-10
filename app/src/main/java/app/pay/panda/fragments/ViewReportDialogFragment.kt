package app.pay.panda.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.R
import app.pay.panda.adapters.ViewReportAdapter
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.viewreportdialog.Data
import app.pay.panda.responsemodels.viewreportdialog.ViewReportDialogResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class ViewReportDialogFragment : DialogFragment() {

    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var viewlist: MutableList<Data>

    companion object {
        private const val ARG_KEY = "ARG_KEY"

        fun newInstance(value: String): ViewReportDialogFragment {
            val fragment = ViewReportDialogFragment()
            val args = Bundle()
            args.putString(ARG_KEY, value)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentActivity) {
            myActivity = context
            userSession = UserSession(context)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_report_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycle)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val value = arguments?.getString(ARG_KEY)
        if (value != null) {
            viewReport("0", "10", value, recyclerView)
        }
    }

    private fun viewReport(pageNo: String, txnCount: String, to: String, recyclerView: RecyclerView) {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.viewReport(requireContext(), token, pageNo, txnCount, to, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                try {
                    val response: ViewReportDialogResponse = Gson().fromJson(message, ViewReportDialogResponse::class.java)
                    if (!response.error && response.data != null) {
                        viewlist = response.data

                        // Set up the RecyclerView adapter
                        val adapter = ViewReportAdapter(myActivity, viewlist)
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE
                    }else{

                        Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                        //findNavController().popBackStack()
                    }
                } catch (e: JsonSyntaxException) {
                    e.printStackTrace()
                }
            }

            override fun fail(from: String) {
                // Handle failure
            }
        })
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}
