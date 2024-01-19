package cl.jam.p2_evaluacion3.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Client(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String,
    var birdDate: LocalDate,
    var email: String,
    var phone: String,
    var dniFront: String = "",
    var dniBack: String = "",
    var lat: Int = 0,
    var long: Int = 0,
    var creationDate: LocalDate = LocalDate.now()
)