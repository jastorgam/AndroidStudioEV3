package cl.jam.p2_evaluacion3.data

import android.annotation.SuppressLint
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import java.util.concurrent.Executor
import java.util.function.Consumer

class LocationRepository(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationManager: LocationManager?,
    private val executor: Executor? = null
) {


    @SuppressLint("MissingPermission")
    fun getLocationLM(
        onSuccess: (u: Location) -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        locationManager?.getCurrentLocation(
            LocationManager.GPS_PROVIDER,
            null,
            executor!!,
            Consumer {
                if (it != null) {
                    onSuccess(it)
                } else {
                    onError(Exception("Error al conseguir ubicacion"))
                }
            }
        )
    }


    @SuppressLint("MissingPermission")
    fun getLocation(
        onSuccess: (u: Location) -> Unit,
        onError: (e: Exception) -> Unit
    ) {
        val task = fusedLocationProviderClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY, null
        )
        task.addOnSuccessListener { onSuccess(it) }
        task.addOnFailureListener { onError(it) }
    }
}