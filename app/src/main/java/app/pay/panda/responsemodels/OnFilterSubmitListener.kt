package app.pay.panda.responsemodels

interface OnFilterSubmitListener {
    fun onFilterSubmit(startDate: String, endDate: String, department: String, priority: String, status: String, count: Int)
}
