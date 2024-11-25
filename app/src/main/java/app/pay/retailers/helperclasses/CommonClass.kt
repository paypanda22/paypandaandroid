package app.pay.retailers.helperclasses

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import java.text.SimpleDateFormat
import java.util.Date
import android.net.wifi.WifiManager
import android.content.Context
import android.os.Build
import android.provider.Settings;
import android.util.Base64
import java.util.Locale
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.FragmentActivity
import app.pay.retailers.interfaces.MCallBackResponse
import app.pay.retailers.responsemodels.dashBoardData.DashBoardData
import app.pay.retailers.retrofit.Constant
import app.pay.retailers.retrofit.UtilMethods
import com.google.gson.Gson
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.time.*

class CommonClass {
    companion object {
        fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*[A-Za-z0-9])?@[A-Za-z0-9]+\\.[A-Za-z]{2,}$".toRegex()
            return email.matches(emailRegex)
        }
        fun getDeviceId(activity: Activity):String{
            @SuppressLint("HardwareIds") val androidID: String = Settings.Secure.getString(activity.contentResolver, Settings.Secure.ANDROID_ID)
            val pModel = Build.MODEL
            val manufacture = Build.MANUFACTURER
            val device = Build.DEVICE
            return "$androidID-$pModel-$manufacture-$device"
        }


//        fun getGeoAddress(context: Context, latlong: LatLng): String {
//
//            // Use Geocoder to get the address based on the LatLng
//            val geocoder = Geocoder(context, Locale.getDefault())
//            return try {
//                val addresses = geocoder.getFromLocation(latlong.latitude, latlong.longitude, 1)
//                if (addresses!!.isNotEmpty()) {
//                    addresses!![0].getAddressLine(0)
//                } else {
//                    ""
//                }
//            } catch (e: IOException) {
//                e.printStackTrace()
//                ""
//            }
//        }

        fun toSentenceCase(string: String): String {
            var result = ""
            var capitalizeNext = true

            string.forEach { char ->
                when {
                    capitalizeNext && char.isLetter() -> {
                        result += char.uppercaseChar()
                        capitalizeNext = false
                    }

                    char == '.' || char == '?' || char == '!' -> {
                        result += char
                        capitalizeNext = true
                    }

                    else -> result += char
                }
            }

            return result
        }


        /** get Live Time app format provided*/
        fun getLiveTime(format: String): String {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(Date())
        }

        fun isTime1GreaterThanTime2(time1: String, time2: String): Boolean {
            val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

            val date1 = format.parse(time1)
            val date2 = format.parse(time2)

            return date1 > date2
        }



        fun copyTextToClipboard(context: Context, text: String) {
            // Get the clipboard system service
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a ClipData object holding the text to copy
            val clip = ClipData.newPlainText("label", text)

            // Set the ClipData to the clipboard
            clipboard.setPrimaryClip(clip)
        }

        fun getCurrentLatLogString(userSession: UserSession):String{
            return userSession.getLocationData(Constant.M_LAT).toString()+","+userSession.getLocationData(Constant.M_LONG).toString()
        }

//

        fun getTimeDifference(providedTimeString: String, timeZone: String = "Asia/Kolkata"): String {
           val currTime= getLiveTime("HH:mm:ss")
            val(currHr,currMin,currSec)=currTime.split(":").map { it.toInt() }
            val (provHr,provMin,provSec)=providedTimeString.split(":").map { it.toInt() }
            val difHr=currHr-provHr
            val difMin=currMin-provMin
            val difSec=currSec-provSec

            val hours = difHr*60
            val minutes = difMin
            val seconds = if (difSec<60){1} else {0}

            val totalDiff=hours+minutes+seconds



            // 4. Format Difference
            return String.format("%02d",totalDiff)
        }

        fun hideKeyBoard(myActivity: Activity, view: View) {
            val imm : InputMethodManager = myActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showDatePickerDialog(myActivity:Activity,editText:EditText) {
            // Get current date
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                myActivity,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                    // Format the date and set it to EditText
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(selectedYear, selectedMonth, selectedDayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    editText.setText(dateFormat.format(selectedDate.time))
                },
                year, month, day
            )
            datePickerDialog.datePicker.maxDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        fun getDashBoardData(requireActivity: FragmentActivity,userSession: UserSession) {
            val token = userSession.getData(Constant.USER_TOKEN).toString()
            UtilMethods.dashBoardData(requireActivity, token, object : MCallBackResponse {
                override fun success(from: String, message: String) {
                    val response: DashBoardData = Gson().fromJson(message, DashBoardData::class.java)
                    response.data?.let { userSession.setUserData(it) }
                }

                override fun fail(from: String) {
                }
            })
        }

        fun getIPAddress(context: Context): String {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress

            // Convert IP address integer to string
            return String.format(
                Locale.getDefault(), "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )
        }
        fun strReplace(input: String, search: String, replace: String): String {
            return input.replace(search, replace)
        }
        fun base64urlEncode(text: Any): Any {
            var encodedText = Base64.encodeToString(text.toString().toByteArray(Charsets.UTF_8), Base64.NO_WRAP)
            encodedText = strReplace(encodedText, "+", "-")
            encodedText = strReplace(encodedText, "/", "_")
            encodedText = strReplace(encodedText, "=", "")
            return encodedText
        }

        fun maskAadhaar(aadharNumber: String):String {
            val maskedPart = "xxxx-xxxx-"
            val lastFourDigits = aadharNumber.takeLast(4)
            return maskedPart + lastFourDigits
        }

        fun formatDateTime(inputTime: String): String {
            val instant = Instant.parse(inputTime)

            // Convert to desired time zone, if needed
            val zoneId = ZoneId.of("Asia/Kolkata")
            val zonedDateTime = instant.atZone(zoneId)

            // Format the timestamp
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return zonedDateTime.format(formatter)
        }
        fun parseAndFormatDateTime(dateTimeString: String): String {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            val dateTime = LocalDateTime.parse(dateTimeString, formatter)

            // Formatting it back to remove the 'T'
            return dateTime.format(formatter)
        }




    }



}

