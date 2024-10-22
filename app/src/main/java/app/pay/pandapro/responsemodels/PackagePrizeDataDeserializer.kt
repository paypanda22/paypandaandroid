package app.pay.pandapro.responsemodels

import app.pay.pandapro.responsemodels.realvalueresponse.Data
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PackagePrizeDataDeserializer  : JsonDeserializer<List<Data>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Data> {
        val dataList = mutableListOf<Data>()

        if (json != null) {
            if (json.isJsonArray) {
                // If it's an array, deserialize it as a list
                json.asJsonArray.forEach { element ->
                    dataList.add(Gson().fromJson(element, Data::class.java))
                }
            } else if (json.isJsonObject) {
                // If it's an object, add it as a single item in the list
                dataList.add(Gson().fromJson(json, Data::class.java))
            }
        }

        return dataList
    }
}