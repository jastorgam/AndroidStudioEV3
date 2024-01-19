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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.jam.p2_evaluacion3.data.Client
import cl.jam.p2_evaluacion3.data.LocationRepository
import cl.jam.p2_evaluacion3.ui.viewmodels.ClientsViewModel
import coil.compose.AsyncImage
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Preview
@Composable
fun RequestAccountUI(
    vm: ClientsViewModel = viewModel(factory = ClientsViewModel.Factory),
    navToLogin: () -> Unit = {}
) {
    Log.v("Request", "Init")

    val context = LocalContext.current

    var msg by rememberSaveable { mutableStateOf<String?>(null) }
    var isDialogFrontOpen by remember { mutableStateOf(false) }
    var isDialogBackOpen by remember { mutableStateOf(false) }
    var isDialogMessage by remember { mutableStateOf(false) }
    var msgDialogMessage by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    var invalidDate by remember { mutableStateOf(false) }

    val (uriFront, setUriFront) = remember { mutableStateOf<Uri?>(null) }
    val (uriBack, setUriBack) = remember { mutableStateOf<Uri?>(null) }
    val (picFrontOk, setPicFrontOk) = remember { mutableStateOf(false) }
    val (picBackOk, setPicBackOk) = remember { mutableStateOf(false) }


    var name by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var birdDate by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var lat by remember { mutableDoubleStateOf(0.0) }
    var lon by remember { mutableDoubleStateOf(0.0) }

    var thereAreMistakes by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.getClients()
    }

    val launchPicFront = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            Log.v("Picture Front", "${it.toString()} - ${uriFront}")
            setPicFrontOk(it)
        }
    )

    val launchPicBack = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            Log.v("Picture Back", "${it.toString()} - ${uriFront}")
            setPicBackOk(it)
        }
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
                        lat = it.latitude
                        lon = it.longitude
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
                                lat = it.latitude
                                lon = it.longitude
                            },
                            onError = {
                                Log.e("Location", it.message.toString())
                                msg = "ERROR: No se puedo recuperar ubicacion"
                                lat = 0.0
                                lon = 0.0
                            }
                        )

                    })
            } else {
                msg = "ERROR: No hay permisos"
                Log.e("Location", "ERROR: No hay permisos")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Face, contentDescription = "",
                modifier = Modifier.size(32.dp)
            )
//            Spacer(modifier = Modifier.width(4.dp))
            Text(
                "Solicitud de Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = rut,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    rut = it
                }
            },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = birdDate,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    birdDate = it
                }
            },
            label = { Text("📅 Fecha Nacimiento ddMMyyyy") },
            modifier = Modifier.fillMaxWidth(),
        )
        if (invalidDate) {
            Text(
                "Formato de fecha incorrecto",
                color = Color.Red
            )
        }
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("📧 Email") },
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = phone,
            onValueChange = {
                if (it.all { char -> char.isDigit() }) {
                    phone = it
                }
            },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val fileName = "${getFileName()}.jpg"
                    val localUri = createPublicFile(context, fileName)
                    setUriFront(localUri)
                    launchPicFront.launch(localUri)
                },
//                modifier = Modifier
//                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cedula Frontal")
            }
            if (picFrontOk) {

                Button(
                    onClick = { isDialogFrontOpen = true },
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Text("👁️")
                }
            }
        }
//        Text(uriFront.toString())

        if (isDialogFrontOpen) {
            CustomDialog(onDismiss = { isDialogFrontOpen = false }, uri = uriFront)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val fileName = "${getFileName()}.jpg"
                    val localUri = createPublicFile(context, fileName)
                    setUriBack(localUri)
                    launchPicBack.launch(localUri)
                },
//                modifier = Modifier
//                    .fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cédula Trasera")
            }

            if (picBackOk) {
                Button(
                    onClick = { isDialogBackOpen = true },
                    modifier = Modifier
                        .wrapContentSize()
                ) {
                    Text("👁️")
                }
            }

        }
//        Text(uriBack.toString())
        if (isDialogBackOpen) {
            CustomDialog(onDismiss = { isDialogBackOpen = false }, uri = uriBack)
        }

        if (thereAreMistakes) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                "Necesita llenar todos los valores",
                color = Color.Red
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                invalidDate = false
                thereAreMistakes = name.isBlank() ||
                        rut.isBlank() ||
                        birdDate.isBlank() ||
                        email.isBlank() ||
                        phone.isBlank() ||
                        uriFront == null ||
                        uriBack == null;

                if (!thereAreMistakes) {

                    // Validacion de la fecha.. no pude usar un datepicker
                    try {
                        val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
                        LocalDate.parse(birdDate, formatter)
                    } catch (e: Exception) {
                        Log.e("Fecha", "${e.message}")
                        invalidDate = true
                    }

                    if (!invalidDate) {
                        isLoading = true
                        msg = null
                        launchLocation.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Solicitar")
        }

        if (isLoading) {
            // Muestra el CircularProgressIndicator si isLoading es true
            CircularProgressIndicator(
                modifier = Modifier
                    .size(48.dp)
                    .padding(16.dp)
            )
        }

        // Aca espara a que msg tenga algun valor retornado desde location
        // Que se demora mucho en responder y no me guardaba los valores.
        msg?.let {
            isLoading = false
            val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
            val localDate = LocalDate.parse(birdDate, formatter)

            val newClient = Client(
                name = name,
                birdDate = localDate,
                email = email,
                phone = phone,
                dniFront = uriFront.toString(),
                dniBack = uriBack.toString(),
                lat = lat,
                lon = lon,
                creationDate = LocalDate.now()
            )

            vm.addClient(newClient)
            vm.getClients().forEach {
                Log.v("Desde UI", "${it.id} - ${it.name}")
            }

            if (msg!!.startsWith("ERROR")) {
                msgDialogMessage = "Error al recuperar la ubicación. Se dejarán valores en 0"
                isDialogMessage = true
            } else {
                msg = null
                navToLogin()
            }
        }

        if (isDialogMessage) {
            CustomDialogMsg(onDismiss = {
                isDialogMessage = false
                navToLogin()
            }, msg = msgDialogMessage)
        }

    }


//    Column {
//        Text(msg)
//        Button(onClick = {
//            msg = "Obteniendo Ubicación"
//            launchLocation.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }) { Text(text = "Recuperar ubicacion") }
//
//        Button(onClick = {
//            msg = "Foto"
//            val fileName = "${getFileName()}.jpg"
//            val localUri = createPublicFile(context, fileName)
//            setUriFront(localUri)
//            launchPicFront.launch(localUri)
//        }) {
//            Text(text = "Tomar Foto")
//
//        }
//        if (picFrontOk) {
//            Text(uriFront.toString())
//            AsyncImage(model = uriFront, contentDescription = null)
//        }
//    }

}

@Composable
fun CustomDialog(onDismiss: () -> Unit, uri: Uri?) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        // Contenido de la ventana modal
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text("Contenido de la Ventana Modal")
            AsyncImage(model = uri, contentDescription = null)

            // Botón para cerrar la ventana modal
            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 16.dp)
            ) {
                Text("Cerrar")
            }
        }
    }
}

@Composable
fun CustomDialogMsg(onDismiss: () -> Unit, msg: String) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {

        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(msg)

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(top = 16.dp)
            ) {
                Text("Cerrar")
            }
        }
    }
}


fun getFileName() =
    SimpleDateFormat("yyyyMMddHHmmss", Locale("es-CL")).format(Calendar.getInstance().time)

fun createPublicFile(
    context: Context,
    fileName: String,
    mime: String = "image/jpeg"
): Uri? = ContentValues().run {
    put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
    put(MediaStore.MediaColumns.MIME_TYPE, mime)
    context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        this
    )
}




