package com.codefal.mystoryapp.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import com.codefal.mystoryapp.R
import java.lang.NullPointerException

@Suppress("DEPRECATION")
fun convertLatLngToAddressForAdapter(lat: Double, lon: Double, context: Context): String{
    var message = ""
    if (lat != 0.0 && lon != 0.0 ) {
        if (lat >= -90 && lat <= 90) {
            val geocoder = Geocoder(context)
            val addresses = geocoder.getFromLocation(lat, lon, 1) as List<Address>
            if (addresses.isNotEmpty()) {
                try {
                    message = buildString {
                        append(addresses[0].locality).append(",").append(addresses[0].subAdminArea)
                    }
                }catch (e: NullPointerException){
                    Log.d("TAG", "convertLatLngToAddressForAdapter: ${e.message.toString()}")
                }
            } else message = "0.0"
        }else{
            message =  context.getString(R.string.illegal_arg)
        }
    }else{
        message =  context.getString(R.string.loc_not_found)
    }
    return message
}