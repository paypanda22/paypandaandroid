package app.pay.retailers

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import app.pay.retailers.activity.ActivityCheckPermissions
import app.pay.retailers.helperclasses.AllPermissions
import app.pay.retailers.helperclasses.UserSession

abstract class BaseFragment<T : ViewBinding>(
    private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T) : Fragment() {

    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            val permissions = activity?.let { AllPermissions(it, UserSession(requireActivity())) }
            val plist = permissions?.checkPermissions()
            if (plist != null) {
                if (plist.isNotEmpty()) {
                    val firstPermission = plist[0]
                    startActivity(Intent(activity, ActivityCheckPermissions::class.java).apply {
                        putExtra("permission", firstPermission.name)
                        putExtra("type", firstPermission.type)
                    })
                }else{
                    init()
                    addListeners()
                    setData()
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun init()
    protected abstract fun addListeners()
    protected abstract fun setData()


}
