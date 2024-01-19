package cl.jam.p2_evaluacion3.ui.composables

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.location.LocationManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import cl.jam.p2_evaluacion3.data.LocationRepository
import cl.jam.p2_evaluacion3.ui.viewmodels.ClientsViewModel
import com.google.android.gms.location.LocationServices
import  androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Preview
@Composable
fun RequestAccountUI(
    viewModel: ClientsViewModel = viewModel(factory = ClientsViewModel.Factory),
    navToLogin: () -> Unit = {}
) {
    Log.v("Request", "Init")

    val context = LocalContext.current

    var msg by rememberSaveable { mutableStateOf("Ubicacion:") }
    val (uri, setUri) = remember { mutableStateOf<Uri?>(null) }
    val (pictureOk, setPictureOk) = remember { mutableStateOf(false) }

    val launchPicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { setPictureOk(it) }
    )

    val launchLocation = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            if (it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            ) {
                val locationService = LocationServices.getFusedLocationProviderClient(context)
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val repository =
                    LocationRepository(locationService, locationManager, context.mainExecutor)

                repository.getLocation(
                    onSuccess = {
                        Log.v("Location", "Recupero Google ${it.latitude}-${it.longitude}")
                        msg = "Lat : ${it.latitude} - Lon: ${it.longitude}"
                    },
                    onError = {
                        Log.v("Location", "LocationManager")
                        repository.getLocationLM(
                            onSuccess = {
                                Log.v(
                                    "Location",
                                    "Recupero LocationManager ${it.latitude}-${it.longitude}"
                                )
                                msg = "Lat : ${it.latitude} - Lon: ${it.longitude}"
                            },
                            onError = {
                                Log.e("Location", it.message.toString())
                                msg = "No se puedo recuperar ubicacion"
                            }
                        )

                    })
            } else {
                msg = "No hay permisos"
            }
        })

    Column {
        Text(msg)
        Button(onClick = {
            msg = "proando"
            launchLocation.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }) {
            Text(text = "Recuperar ubicacion")

        }
    }

}


fun getFileName() =
    SimpleDateFormat("yyyyMMddHHmmss", Locale("es-CL")).format(Calendar.getInstance().time)

fun createPublicFile(
    context: Context,
    name: String,
    mime: String = "image/jpeg"
): Uri? = ContentValues().run {
    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
    put(MediaStore.MediaColumns.MIME_TYPE, mime)
    context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        this
    )
}




