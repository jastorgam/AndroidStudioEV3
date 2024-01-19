package cl.jam.p2_evaluacion3.ui.composables


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.jam.p2_evaluacion3.ui.viewmodels.ClientsViewModel


@Preview
@Composable
fun LoginAccountUI(
    navToRequest: () -> Unit = {},
    vm: ClientsViewModel = viewModel(factory = ClientsViewModel.Factory)
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
//        vm.deleteTable()
        vm.getClients()
    }


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
                "IplaBank",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Usuario") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            leadingIcon = {
                Icon(Icons.Default.AccountBox, contentDescription = "Usuario")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = "Ingresar")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ingresar")
        }

        Button(
            onClick = { navToRequest() },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Request Account")
            Spacer(modifier = Modifier.width(8.dp))
            Text("Solicitar Cuenta")
        }
        Spacer(modifier = Modifier.width(20.dp))
        Text("Listado desde BD")
        LazyColumn {
            items(vm.clientes) {
                Text("${it.id} - ${it.toString()}")
            }
        }
    }

}