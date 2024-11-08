package app.pay.pandapro.fragments.home

import android.content.Intent
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.pandapro.BaseFragment
import app.pay.pandapro.activity.IntroActivity
import app.pay.pandapro.adapters.NotificationListAdapter
import app.pay.pandapro.databinding.FragmentNotificationsBinding
import app.pay.pandapro.helperclasses.UserSession
import app.pay.pandapro.interfaces.MCallBackResponse
import app.pay.pandapro.responsemodels.notification.Notification
import app.pay.pandapro.responsemodels.notification.NotificationListResponse
import app.pay.pandapro.retrofit.Constant
import app.pay.pandapro.retrofit.UtilMethods
import com.google.gson.Gson


class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>(FragmentNotificationsBinding::inflate) {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private lateinit var list: MutableList<Notification>
    override fun init() {
        nullActivityCheck()
        userSession= UserSession(requireContext())

        getNotificationList()

    }

    private fun getNotificationList() {
        binding.imageView.visibility=VISIBLE
        binding.rvNotifications.visibility= GONE
        binding.llNoData.visibility= GONE
        val token=userSession.getData(Constant.USER_TOKEN).toString()
        UtilMethods.getNotifications(requireContext(),"",false,token,object:MCallBackResponse{
            override fun success(from: String, message: String) {
                val response:NotificationListResponse= Gson().fromJson(message,NotificationListResponse::class.java)
                if (!response.error){
                    if (response.data.notifications.isNotEmpty()){
                        list= mutableListOf()
                        list.addAll(response.data.notifications)
                        val notificationAdapter=NotificationListAdapter(myActivity,list)
                        binding.rvNotifications.adapter=notificationAdapter
                        binding.rvNotifications.layoutManager=LinearLayoutManager(myActivity)

                        binding.imageView.visibility= GONE
                        binding.rvNotifications.visibility= VISIBLE
                        binding.llNoData.visibility= GONE
                    }else{
                        binding.imageView.visibility= GONE
                        binding.rvNotifications.visibility= GONE
                        binding.llNoData.visibility= VISIBLE
                    }
                }else{
                    binding.imageView.visibility= GONE
                    binding.rvNotifications.visibility= GONE
                    binding.llNoData.visibility= VISIBLE
                }
            }

            override fun fail(from: String) {
                binding.imageView.visibility= GONE
                binding.rvNotifications.visibility= GONE
                binding.llNoData.visibility= VISIBLE
            }
        })

    }

    private fun nullActivityCheck() {
        if (activity!=null){
            myActivity=activity as FragmentActivity
        }else{
            startActivity(Intent(context,IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun setData() {

    }

}