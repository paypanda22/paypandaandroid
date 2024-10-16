import app.pay.paypanda.responsemodels.dmtBeneficiaryList.Data
import app.pay.paypanda.responsemodels.dmtBeneficiaryList.RecipientListResponse
import com.google.common.reflect.TypeToken
import com.google.gson.JsonDeserializationContext
import com.google.gson.*
import java.lang.reflect.Type


class RecipientListDeserializer : JsonDeserializer<RecipientListResponse> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): RecipientListResponse {
        val jsonObject = json?.asJsonObject ?: JsonObject()

        val error = jsonObject.get("error")?.asBoolean ?: false
        val message = jsonObject.get("message")?.asString ?: ""
        val statusCode = jsonObject.get("statusCode")?.asInt ?: 0

        // Handle "data" field which could be either an array or an object
        val dataField = jsonObject.get("data")
        val dataList = mutableListOf<Data>()

        if (dataField?.isJsonArray == true) {
            // Handle the case where "data" is an array
            val listType: Type = object : TypeToken<List<Data>>() {}.type
            dataList.addAll(context?.deserialize(dataField.asJsonArray, listType) ?: listOf())
        } else if (dataField?.isJsonObject == true) {
            // Handle the case where "data" is a single object
            dataList.add(context?.deserialize(dataField.asJsonObject, Data::class.java) ?: Data())
        }

        return RecipientListResponse(
            data = dataList,
            error = error,
            message = message,
            statusCode = statusCode
        )
    }
}