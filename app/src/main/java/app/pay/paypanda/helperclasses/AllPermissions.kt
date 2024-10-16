package app.pay.paypanda.helperclasses

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.core.content.ContextCompat
import app.pay.paypanda.retrofit.Constant


class AllPermissions(
    private val activity: Activity,
    private val userSession: UserSession
) {
    private val permissionList = mutableListOf<PermissionList>()
    fun checkPermissions(): List<PermissionList> {
        return if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(PermissionList(Manifest.permission.CAMERA, "camera", false))
            permissionList
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(
                PermissionList(
                    Manifest.permission.POST_NOTIFICATIONS,
                    "Notification",
                    false
                )
            )
            permissionList
        } else {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionList.add(
                    PermissionList(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        "location",
                        false
                    )
                )
            } else {
                saveLocation();
                permissionList.clear()
            }
            permissionList
        }
    }


    private fun saveLocation() {
        LocationUtils.init(activity)
        if (LocationUtils.isLocationEnabled(activity)) {
            LocationUtils.getCurrentLocation(activity, object :
                LocationUtils.LocationCallbackWrapper() {
                override fun onLocationResult(location: Location) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    userSession.setLocationData(Constant.M_LAT, latitude)
                    userSession.setLocationData(Constant.M_LONG, longitude)
                }
            })
        } else {
//            ShowDialog.showDialog(activity,"Location Not Enabled","Please Enable Location to proceed","error",
//                object : MyClick {
//                    override fun onClick() {
//                        LocationUtils.openLocationSettings(activity)
//                    }
//                })

        }
    }
}