package app.pay.panda.fragments.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.NotificationListAdapter
import app.pay.panda.databinding.FragmentNotificationsBinding
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.responsemodels.notification.Notification
import app.pay.panda.responsemodels.notification.NotificationListResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
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
        UtilMethods.getNotifications(requireContext(),"50",false,token,object:MCallBackResponse{
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