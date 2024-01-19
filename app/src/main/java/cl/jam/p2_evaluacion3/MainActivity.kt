package cl.jam.p2_evaluacion3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import cl.jam.p2_evaluacion3.ui.composables.AppNavBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavBar()
        }
    }

    private val lauchTakePicture = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { pictureOk ->
        Log.v("Picture", pictureOk.toString())
        if (pictureOk) {

        }

    }
}

