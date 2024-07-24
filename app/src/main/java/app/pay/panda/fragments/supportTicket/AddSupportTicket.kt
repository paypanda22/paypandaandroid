package app.pay.panda.fragments.supportTicket

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.pay.panda.BaseFragment
import app.pay.panda.R
import app.pay.panda.activity.IntroActivity
import app.pay.panda.adapters.DisputeMastersAdapter
import app.pay.panda.databinding.DialogDisputeMasterBinding
import app.pay.panda.databinding.FragmentAddSupportTicketBinding
import app.pay.panda.databinding.LytDialogAddRecipientBinding
import app.pay.panda.helperclasses.ActivityExtensions
import app.pay.panda.helperclasses.ShowDialog
import app.pay.panda.helperclasses.UserSession
import app.pay.panda.helperclasses.Utils.Companion.showToast
import app.pay.panda.interfaces.DepartmentClickLickListner
import app.pay.panda.interfaces.MCallBackResponse
import app.pay.panda.interfaces.MyClick
import app.pay.panda.interfaces.PriorityClickLickListner
import app.pay.panda.interfaces.ServicesClickLickListner
import app.pay.panda.responsemodels.addTicket.AddSupportTicketResponse
import app.pay.panda.responsemodels.disputeMaster.Department
import app.pay.panda.responsemodels.disputeMaster.DisputeMasterResponse
import app.pay.panda.responsemodels.disputeMaster.Priority
import app.pay.panda.responsemodels.disputeMaster.Service
import app.pay.panda.responsemodels.uploadImage.UploadImageResponse
import app.pay.panda.retrofit.Constant
import app.pay.panda.retrofit.UtilMethods
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import java.io.File


class AddSupportTicket : BaseFragment<FragmentAddSupportTicketBinding>(FragmentAddSupportTicketBinding::inflate), DepartmentClickLickListner,
    PriorityClickLickListner, ServicesClickLickListner {
    private lateinit var userSession: UserSession
    private lateinit var myActivity: FragmentActivity
    private var service_id = ""
    private var priority = ""
    private var department_id = ""
    private lateinit var attachments: MutableList<String>
    private lateinit var alertDialog: AlertDialog

    private lateinit var departmentList: MutableList<Department>
    private lateinit var priorityList: MutableList<Priority>
    private lateinit var servicestList: MutableList<Service>
    private var fileType = ""

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val fileUri = data?.data!!
                    val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)

                    when (fileType) {
                        "attachment1" -> {
                            binding.attachment1.visibility = VISIBLE
                            binding.iv1.visibility = GONE
                            binding.attachment1.setImageBitmap(bitmap)
                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "attachment2" -> {
                            binding.attachment2.visibility = VISIBLE
                            binding.iv2.visibility = GONE
                            binding.attachment2.setImageBitmap(bitmap)
                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "attachment3" -> {
                            binding.attachment3.visibility = VISIBLE
                            binding.iv3.visibility = GONE
                            binding.attachment3.setImageBitmap(bitmap)
                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }

                        "attachment4" -> {
                            binding.attachment4.visibility = VISIBLE
                            binding.iv4.visibility = GONE
                            binding.attachment4.setImageBitmap(bitmap)
                            val path: String = getRealPathFromURI(fileUri)
                            val file: File = File(path)
                            uploadImage(file)
                        }


                        else -> {
                            Toast.makeText(requireContext(), "Invalid Document Type Selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                ImagePicker.RESULT_ERROR -> {
                    Toast.makeText(myActivity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(myActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }

            }

        }

    override fun init() {
        nullActivityCheck()
        userSession = UserSession(requireContext())
        attachments = mutableListOf()
        getAllMasterData()


    }

    private fun getAllMasterData() {
        binding.llNoData.visibility = GONE
        binding.llMainContent.visibility = GONE
        binding.imageView.visibility = VISIBLE
        UtilMethods.getDisputeMasters(requireContext(), object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: DisputeMasterResponse = Gson().fromJson(message, DisputeMasterResponse::class.java)
                if (!response.error) {
                    // Check if department list is not empty
                    if (response.data.department.isNotEmpty()) {
                        binding.edtDepartment.setText(response.data.department[0].department)
                        department_id = response.data.department[0]._id
                    }

                    // Check if priority list is not empty
                    if (response.data.priority.isNotEmpty()) {
                        binding.edtPriority.setText(response.data.priority[0].priority)
                        priority = response.data.priority[0]._id
                    }

                    // Check if services list is not empty
                    if (response.data.services.isNotEmpty()) {
                        binding.edtService.setText(response.data.services[0].service_name)
                        service_id = response.data.services[0]._id
                    }

                    departmentList = mutableListOf()
                    departmentList.addAll(response.data.department)

                    priorityList = mutableListOf()
                    priorityList.addAll(response.data.priority)

                    servicestList = mutableListOf()
                    servicestList.addAll(response.data.services)

                    binding.edtSubject.requestFocus()

                    binding.llNoData.visibility = GONE
                    binding.llMainContent.visibility = VISIBLE
                    binding.imageView.visibility = GONE
                } else {
                    binding.llNoData.visibility = VISIBLE
                    binding.llMainContent.visibility = GONE
                    binding.imageView.visibility = GONE
                }
            }

            override fun fail(from: String) {
                binding.llNoData.visibility = VISIBLE
                binding.llMainContent.visibility = GONE
                binding.imageView.visibility = GONE
            }
        })
    }


    private fun nullActivityCheck() {
        if (activity != null) {
            myActivity = activity as FragmentActivity
        } else {
            startActivity(Intent(context, IntroActivity::class.java))
        }
    }

    override fun addListeners() {
        binding.rlImage1.setOnClickListener {
            fileType = "attachment1"
            takeImage()
        }
        binding.rlImage2.setOnClickListener {
            fileType = "attachment2"
            takeImage()
        }
        binding.rlImage3.setOnClickListener {
            fileType = "attachment3"
            takeImage()
        }
        binding.rlImage4.setOnClickListener {
            fileType = "attachment4"
            takeImage()
        }

        binding.edtDepartment.setOnClickListener {
            openDataDialog("department")
        }
        binding.edtPriority.setOnClickListener {
            openDataDialog("priority")
        }
        binding.edtService.setOnClickListener {
            openDataDialog("Services")
        }

        binding.btnSubmit.setOnClickListener {
            if (binding.edtSubject.text.toString().isEmpty()) {
                binding.edtSubject.error = "Enter Subject of Ticket"
            } else if (binding.edtMessage.text.toString().isEmpty()) {
                binding.edtMessage.error = "Enter Message for Ticket"
            } else {
                addTicket()
            }
        }

    }

    private fun addTicket() {
        val token = userSession.getData(Constant.USER_TOKEN).toString()
        val requestData = hashMapOf<String, Any?>()
        requestData["type"] = department_id
        requestData["contact_no"] = userSession.getData(Constant.MOBILE).toString()
        requestData["subject"] = binding.edtSubject.text.toString()
        requestData["service_id"] = service_id
        requestData["priority"] = priority
        requestData["description"] = binding.edtMessage.text.toString()
        requestData["attachments"] = attachments
        requestData["user_id"] = token
        UtilMethods.addSupportTicket(requireContext(), requestData, object : MCallBackResponse {
            override fun success(from: String, message: String) {
                val response: AddSupportTicketResponse = Gson().fromJson(message, AddSupportTicketResponse::class.java)
                if (!response.error) {
                    ShowDialog.bottomDialogSingleButton(myActivity, "SUCCESS", response.message, "success", object : MyClick {
                        override fun onClick() {
                            findNavController().popBackStack()
                        }
                    })
                } else {
                    ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", response.message, "error", object : MyClick {
                        override fun onClick() {

                        }
                    })
                }
            }

            override fun fail(from: String) {
                ShowDialog.bottomDialogSingleButton(myActivity, "ERROR", from, "error", object : MyClick {
                    override fun onClick() {

                    }
                })
            }
        })
    }

    private fun openDataDialog(type: String) {
        // Inflate the dialog layout using ViewBinding
        val dBinding = DialogDisputeMasterBinding.inflate(myActivity.layoutInflater)

        // Create the dialog
        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dBinding.root)
        }
        if (type == "department") {
            dBinding.tvTitle.text = getString(R.string.select_department)
        } else if (type == "priority") {
            dBinding.tvTitle.text = getString(R.string.select_ticket_priority)
        } else {
            dBinding.tvTitle.text = getString(R.string.select_related_service)
        }
        val adapter = DisputeMastersAdapter(
            myActivity,
            departmentList,
            priorityList,
            servicestList,
            this@AddSupportTicket,
            this@AddSupportTicket,
            this@AddSupportTicket,
            type
        )
        dBinding.rvDisputeMaster.adapter = adapter
        dBinding.rvDisputeMaster.layoutManager = LinearLayoutManager(myActivity)


        alertDialog = dialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(true)
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
    }

    override fun setData() {

    }

    private fun uploadImage(file: File) {
        myActivity.let {
            myActivity
            UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                    Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    attachments.add(response.data?.url.toString())
                }

                override fun fail(from: String) {
                    Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }

    private fun takeImage() {
        ImagePicker.with(this)
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .crop(1f, 2f)
            .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }

    private fun getRealPathFromURI(contentURI: Uri): String {
        val result: String
        val cursor = myActivity.contentResolver.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path.toString()
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result

    }

    override fun onItemClicked(holder: RecyclerView.ViewHolder, model: List<Department>, pos: Int) {
        binding.edtDepartment.setText(model[pos].department)
        department_id = model[pos]._id
        alertDialog.dismiss()
    }

    override fun onItemClicked1(holder: RecyclerView.ViewHolder, model: List<Priority>, pos: Int) {
        binding.edtPriority.setText(model[pos].priority)
        priority = model[pos]._id
        alertDialog.dismiss()
    }

    override fun onItemClicked2(holder: RecyclerView.ViewHolder, model: List<Service>, pos: Int) {
        binding.edtService.setText(model[pos].service_name)
        service_id = model[pos]._id
        alertDialog.dismiss()
    }

}