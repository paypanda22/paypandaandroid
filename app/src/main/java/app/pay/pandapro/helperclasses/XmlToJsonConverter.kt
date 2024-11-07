package app.pay.pandapro.helperclasses

import org.json.JSONException
import org.json.XML

class XmlToJsonConverter {
    companion object {
        fun convertXmlToJson(xmlString: String): String {
            var jsonString = ""
            try {
                val jsonObj = XML.toJSONObject(xmlString)
                jsonString = jsonObj.toString(4) // Indent for readability
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return jsonString
        }
    }
}
