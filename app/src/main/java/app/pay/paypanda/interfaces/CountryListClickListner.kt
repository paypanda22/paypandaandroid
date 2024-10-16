package app.pay.paypanda.interfaces

import androidx.recyclerview.widget.RecyclerView
import app.pay.paypanda.responsemodels.countrylist.Data

interface CountryListClickListner {

    fun onItemClicked(holder:RecyclerView.ViewHolder, model:List<Data>, pos:Int)
}