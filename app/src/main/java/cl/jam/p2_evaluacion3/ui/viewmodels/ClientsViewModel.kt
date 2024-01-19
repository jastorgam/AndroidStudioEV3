package cl.jam.p2_evaluacion3.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.jam.p2_evaluacion3.IplaBankApplication
import cl.jam.p2_evaluacion3.data.Client
import cl.jam.p2_evaluacion3.data.ClientDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClientsViewModel(val clientDao: ClientDao) : ViewModel() {

    var clients by mutableStateOf(listOf<Client>())

    suspend fun getClients(): List<Client> {
        viewModelScope.launch(Dispatchers.IO) {
            clients = clientDao.getAll()
        }
        return clients;
    }

    suspend fun addClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            clientDao.insert(client)
            getClients()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val application = (this[APPLICATION_KEY] as IplaBankApplication)
                ClientsViewModel(application.clientDao)
            }
        }
    }


}