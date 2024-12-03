package app.pay.retailers.helperclasses

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class ActivityExtensions {

    companion object {
        fun fullscreen(activity: AppCompatActivity) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        fun isValidMobile(mobile:String):Boolean{
            return if (mobile.length==10){
                mobile.startsWith("6") || mobile.startsWith("7") ||mobile.startsWith("8") || mobile.startsWith("9")
            }else{
                false
            }
        }
        fun isValidName(name: String): Boolean {
            val trimmedName = name.trim()
            val nameRegex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$" // This regex allows letters and spaces only
            return trimmedName.isNotEmpty() && trimmedName.matches(nameRegex.toRegex())
        }

        fun isValidEmail(email:String):Boolean{
            val emailRegex="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"
            return email.matches(emailRegex.toRegex())
        }

        fun isValidPinCode(pinCode:String):Boolean{
            val emailRegex="^[1-9]\\d{5}\$"
            return pinCode.matches(emailRegex.toRegex())
        }

        fun isValidIfsc(ifsc:String):Boolean{
            val emailRegex="^[A-Za-z]{4}0[A-Za-z0-9]{6}\$"
            return ifsc.matches(emailRegex.toRegex())
        }
        fun isValidPan(pan: String): Boolean {
            val pattern = "[a-zA-Z]{5}\\d{4}[a-zA-Z]"
            return pan.matches(pattern.toRegex())
        }

        fun isValidGst(gst: String): Boolean {
            val pattern = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$"
            return gst.matches(pattern.toRegex())
        }

        fun isValidAadhaar(aadhaar: String): Boolean {
            val pattern = "^[2-9][0-9]{11}$"
            return aadhaar.matches(pattern.toRegex())
        }

        fun isValidPassword(password: String): Boolean {
            val pattern = "^(?=.*[a-zA-Z])(?=.*[@\$!&])(?=.*\\d)[A-Z].{8,}\$"
            return password.matches(pattern.toRegex())
        }
    }
}