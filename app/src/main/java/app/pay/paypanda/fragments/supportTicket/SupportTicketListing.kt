    package app.pay.paypanda.fragments.supportTicket

    import android.annotation.SuppressLint
    import android.app.Activity
    import android.app.Dialog
    import android.content.Intent
    import android.graphics.Bitmap
    import android.graphics.Color
    import android.graphics.drawable.ColorDrawable
    import android.net.Uri
    import android.os.Bundle
    import android.provider.MediaStore
    import android.view.Gravity
    import android.view.View
    import android.view.View.GONE
    import android.view.View.VISIBLE
    import android.view.ViewGroup
    import android.view.WindowManager
    import android.widget.AdapterView
    import android.widget.ArrayAdapter
    import android.widget.Toast
    import androidx.activity.result.ActivityResult
    import androidx.activity.result.contract.ActivityResultContracts
    import androidx.core.content.ContextCompat
    import androidx.fragment.app.FragmentActivity
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import app.pay.paypanda.BaseFragment
    import app.pay.paypanda.R
    import app.pay.paypanda.activity.IntroActivity
    import app.pay.paypanda.adapters.SupportTicketListAdapter
    import app.pay.paypanda.databinding.FragmentSupportTicketListingBinding
    import app.pay.paypanda.databinding.LytTicketListBinding
    import app.pay.paypanda.helperclasses.CommonClass
    import app.pay.paypanda.helperclasses.UserSession
    import app.pay.paypanda.helperclasses.Utils.Companion.showToast
    import app.pay.paypanda.interfaces.MCallBackResponse
    import app.pay.paypanda.interfaces.SupportTicketClick
    import app.pay.paypanda.responsemodels.dmtstatus.DmtDisputePriority
    import app.pay.paypanda.responsemodels.dmtstatusfilter.DMTStatusFilter
    import app.pay.paypanda.responsemodels.filter.Data
    import app.pay.paypanda.responsemodels.filter.DepartmentReposne
    import app.pay.paypanda.responsemodels.supportTickets.DataX
    import app.pay.paypanda.responsemodels.supportTickets.SupportTicketListResponse

    import app.pay.paypanda.responsemodels.ticketReply.TicketReplyResponse
    import app.pay.paypanda.responsemodels.uploadImage.UploadImageResponse
    import app.pay.paypanda.retrofit.Constant
    import app.pay.paypanda.retrofit.UtilMethods
    import com.github.dhaval2404.imagepicker.ImagePicker
    import com.google.android.material.textfield.TextInputEditText
    import com.google.gson.Gson
    import java.io.File


    class SupportTicketListing : BaseFragment<FragmentSupportTicketListingBinding>(FragmentSupportTicketListingBinding::inflate), SupportTicketClick {
        private lateinit var userSession: UserSession
        private lateinit var myActivity: FragmentActivity
        private lateinit var list: MutableList<DataX>
        private lateinit var attachments: MutableList<String>
        private lateinit var dBinding: LytTicketListBinding
        var selectedDepartment = ""
        var selectedPriority = ""
        var selectedStatus = ""
        private lateinit var departmentList: MutableList<Data>
        private lateinit var priorityList: MutableList<app.pay.paypanda.responsemodels.dmtstatus.Data>
        private lateinit var statusList: MutableList<app.pay.paypanda.responsemodels.dmtstatusfilter.Data>
        private var count = 50
        private var start_date = ""
        private var end_date = ""
        override fun init() {
            nullActivityCheck()
            userSession = UserSession(requireContext())
            dBinding = LytTicketListBinding.inflate(layoutInflater)
            getSupportTickets(start_date, end_date, "" ?: "", "" ?: "", "" ?: "", 25)
        }

        private fun getSupportTickets(
            startDate: String?,
            endDate: String?,
            selectedDepartment: String,
            selectedPriority: String,
            selectedStatus: String,
            count: Int
        ) {
            binding.rvDisputeList.visibility = GONE
            binding.llNoData.visibility = GONE
            binding.imageView.visibility = VISIBLE
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            val requestData = hashMapOf<String, Any?>()
            requestData["user_id"] = token
            requestData["count"] = count
            requestData["page"] = 0
            requestData["department_id"] = selectedDepartment
            requestData["start_date"] = startDate
            requestData["end_date"] = endDate
            requestData["max_amt"] = ""
            requestData["min_amt"] = ""
            requestData["priority"] = selectedPriority
            requestData["service_id"] = ""
            requestData["sortType"] = ""
            requestData["status"] = selectedStatus
            requestData["txn_id"] = ""
            UtilMethods.getSupportTicketList(
                requireContext(),
                requestData,
                object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        val response: SupportTicketListResponse =
                            Gson().fromJson(message, SupportTicketListResponse::class.java)
                        if (!response.error) {
                            if (response.data.data.isNotEmpty()) {
                                list = mutableListOf()
                                list.addAll(response.data.data)

                                val adapter = SupportTicketListAdapter(
                                    myActivity,
                                    list,
                                    this@SupportTicketListing
                                )
                                binding.rvDisputeList.adapter = adapter
                                binding.rvDisputeList.layoutManager =
                                    LinearLayoutManager(myActivity)


                                binding.rvDisputeList.visibility = VISIBLE
                                binding.llNoData.visibility = GONE
                                binding.imageView.visibility = GONE
                            } else {
                                binding.rvDisputeList.visibility = GONE
                                binding.llNoData.visibility = VISIBLE
                                binding.imageView.visibility = GONE
                            }

                        } else {
                            binding.rvDisputeList.visibility = GONE
                            binding.llNoData.visibility = VISIBLE
                            binding.imageView.visibility = GONE
                        }

                    }

                    override fun fail(from: String) {
                        binding.rvDisputeList.visibility = GONE
                        binding.llNoData.visibility = VISIBLE
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
            binding.filter.setOnClickListener{
                openTransactionFilterDialog()
            }
            binding.ivBack.setOnClickListener{
                findNavController().popBackStack()
            }
            dBinding.spinnerDepartment.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        // Get the selected department object from the list based on position
                        val selectedDepartmentObject = departmentList[position]
                        selectedDepartment = selectedDepartmentObject._id.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Handle when nothing is selected, if necessary
                    }
                }

            dBinding.spinnerPriority.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedPriorityObject= priorityList[position]
                        selectedPriority = selectedPriorityObject._id.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

            dBinding.spinnerStatus.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val selectedStatusObject= statusList[position]
                        selectedStatus = selectedStatusObject._id.toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }

        }

        override fun setData() {

        }

        override fun onItemClicked(
            holder: RecyclerView.ViewHolder,
            model: List<DataX>,
            pos: Int,
            type: String,
            editText: TextInputEditText,
            edt: View
        ) {
            when (type) {
                "reply" -> {
                    val token = userSession.getData(Constant.USER_TOKEN).toString()
                    attachments = mutableListOf()
                    val requestData = hashMapOf<String, Any?>()
                    requestData["user_id"] = token
                    requestData["dispute_id"] = model[pos]._id
                    requestData["chat"] = editText.text.toString()
                    requestData["attachments"] = attachments
                    sendReply(requestData, editText, edt)
                }

                "history" -> {
                    val bundle = Bundle().apply {
                        putString("dispute_id", model[pos]._id)
                    }
                    findNavController().navigate(R.id.supportTicketHistory, bundle)
                }
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

        private val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val fileUri = data?.data!!
                        val bitmap: Bitmap =
                            MediaStore.Images.Media.getBitmap(myActivity.contentResolver, fileUri)
                        //     binding.attachment1.setImageBitmap(bitmap)
                        val path: String = getRealPathFromURI(fileUri)
                        val file: File = File(path)
                        uploadImage(file)
                    }
                }
            }

        private fun uploadImage(file: File) {
            myActivity.let {
                myActivity
                UtilMethods.uploadImage(myActivity, file, object : MCallBackResponse {
                    override fun success(from: String, message: String) {
                        val response: UploadImageResponse = Gson().fromJson(message, UploadImageResponse::class.java)
                        Toast.makeText(myActivity, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        attachments.add(response.data?.url.toString())
                        //binding.txtFileName.text=response.data?.url.toString()
                    }

                    override fun fail(from: String) {
                        Toast.makeText(myActivity, "Unable to Upload Image", Toast.LENGTH_SHORT).show()
                    }
                })
            }

        }
        private fun sendReply(
            requestData: HashMap<String, Any?>,
            editText: TextInputEditText,
            edt: View
        ) {
            UtilMethods.replyToTicket(requireContext(), requestData, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: TicketReplyResponse =
                        Gson().fromJson(message, TicketReplyResponse::class.java)
                    if (!response.error) {
                        editText.text?.clear()
                        showToast(requireContext(), "Reply Send Successfully")
                    } else {
                        showToast(requireContext(), "Unable to Send Reply")
                    }
                    edt.visibility = GONE
                }

                override fun fail(from: String) {
                    edt.visibility = GONE
                    showToast(requireContext(), from)
                }
            })
        }


        private fun department() {
            departmentList = mutableListOf()
            val token = userSession.getData(app.pay.paypanda.retrofit.Constant.USER_TOKEN).toString()

            app.pay.paypanda.retrofit.UtilMethods.department(
                requireContext(),
                token,
                object : MCallBackResponse {
                    @SuppressLint("SetTextI18n")
                    override fun success(from: String, message: String) {
                        val response: DepartmentReposne =
                            Gson().fromJson(message, DepartmentReposne::class.java)
                        if (!response.error) {
                            departmentList.clear()
                            departmentList.addAll(response.data)

                            // Add "Select Department" hint at the first position
                            val departmentNames = mutableListOf("Select Department")
                            departmentNames.addAll(departmentList.map { it.department })

                            // Create ArrayAdapter with the department list including the hint
                            val departmentAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                departmentNames // Use the list with the hint
                            )
                            departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            dBinding.spinnerDepartment.adapter = departmentAdapter

                            // Set default selection to the first item (the hint)
                            dBinding.spinnerDepartment.setSelection(0)

                            // Handle item selection
                            dBinding.spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    // If position 0 (hint) is selected, ignore it
                                    if (position != 0) {
                                        selectedDepartment = departmentList[position - 1]._id.toString()
                                    } else {
                                        selectedDepartment = "" // No department selected
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }
                    }

                    override fun fail(from: String) {
                        Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        private fun dmtDisputePriority() {
            val token = userSession.getData(app.pay.paypanda.retrofit.Constant.USER_TOKEN).toString()

            app.pay.paypanda.retrofit.UtilMethods.dmtDisputePriority(
                requireContext(),
                token,
                object : MCallBackResponse {
                    @SuppressLint("SetTextI18n")
                    override fun success(from: String, message: String) {
                        val response: DmtDisputePriority =
                            Gson().fromJson(message, DmtDisputePriority::class.java)
                        if (!response.error) {
                            priorityList = response.data.toMutableList()

                            // Add hint at the first position
                            val priorityListNames = mutableListOf("Select Priority")
                            priorityListNames.addAll(priorityList.map { it.priority })

                            // Create ArrayAdapter with hint
                            val priorityAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                priorityListNames // Use the list with the hint
                            )
                            priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            dBinding.spinnerPriority.adapter = priorityAdapter

                            // Set default selection to the first item (hint)
                            dBinding.spinnerPriority.setSelection(0)

                            // Handle item selection
                            dBinding.spinnerPriority.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    if (position != 0) { // Ignore the first hint item
                                        selectedPriority = priorityList[position - 1]._id.toString()
                                    } else {
                                        selectedPriority = ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }
                    }

                    override fun fail(from: String) {
                        Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun dmtstatus() {
            val token = userSession.getData(app.pay.paypanda.retrofit.Constant.USER_TOKEN).toString()

            app.pay.paypanda.retrofit.UtilMethods.dmtstatus(
                requireContext(),
                token,
                object : MCallBackResponse {
                    @SuppressLint("SetTextI18n")
                    override fun success(from: String, message: String) {
                        val response: DMTStatusFilter =
                            Gson().fromJson(message, DMTStatusFilter::class.java)
                        if (!response.error) {
                            statusList = response.data.toMutableList()

                            // Add hint at the first position
                            val statusListNames = mutableListOf("Select Status")
                            statusListNames.addAll(statusList.map { it.name })

                            // Create ArrayAdapter with hint
                            val statusAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                statusListNames // Use the list with the hint
                            )
                            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            dBinding.spinnerStatus.adapter = statusAdapter

                            // Set default selection to the first item (hint)
                            dBinding.spinnerStatus.setSelection(0)

                            // Handle item selection
                            dBinding.spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    if (position != 0) { // Ignore the first hint item
                                        selectedStatus = statusList[position - 1]._id.toString()
                                    } else {
                                        selectedStatus = ""
                                    }
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }
                    }

                    override fun fail(from: String) {
                        Toast.makeText(requireContext(), from, Toast.LENGTH_SHORT).show()
                    }
                })
        }

        private fun openTransactionFilterDialog() {
            val filterDialog = Dialog(myActivity)

            // Inflate the layout only if it has not been inflated before
            if (dBinding.root.parent != null) {
                (dBinding.root.parent as ViewGroup).removeView(dBinding.root) // Remove from parent if it has one
            }

            dBinding.root.background =
                ContextCompat.getDrawable(myActivity, R.drawable.curved_background_with_shadow)

            filterDialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            filterDialog.setContentView(dBinding.root) // Set the content view
            filterDialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            filterDialog.window?.attributes?.windowAnimations = R.style.DialogAnimationBottom
            filterDialog.window?.setGravity(Gravity.BOTTOM)

            // Fetch data for spinners and setup listeners
            dmtstatus()
            department()
            dmtDisputePriority()

            val todayDate = CommonClass.getLiveTime("yyyy-MM-dd")
            dBinding.edtFromDate.setText(todayDate)
            dBinding.edtToDate.setText(todayDate)

            dBinding.edtFromDate.setOnClickListener {
                CommonClass.showDatePickerDialog(myActivity, dBinding.edtFromDate)
            }

            dBinding.edtToDate.setOnClickListener {
                if (dBinding.edtFromDate.text.toString().isEmpty()) {
                    dBinding.edtFromDate.error = "Select From Date"
                } else {
                    CommonClass.showDatePickerDialog(myActivity, dBinding.edtToDate)
                }
            }

            dBinding.rgCount.setOnCheckedChangeListener { group, checkedId ->
                count = when (checkedId) {
                    R.id.rb25 -> 25
                    R.id.rb50 -> 50
                    R.id.rb100 -> 100
                    R.id.rb150 -> 150
                    R.id.rb200 -> 200
                    R.id.more -> 400
                    else -> 50
                }
            }
            start_date=dBinding.edtFromDate.text.toString()
            end_date=dBinding.edtToDate.text.toString()
            dBinding.btnSubmit.setOnClickListener {
                getSupportTickets(dBinding.edtFromDate.text.toString(), dBinding.edtToDate.text.toString(), selectedDepartment, selectedPriority, selectedStatus, 25)
                filterDialog.dismiss()
            }

            filterDialog.show()
        }




    }