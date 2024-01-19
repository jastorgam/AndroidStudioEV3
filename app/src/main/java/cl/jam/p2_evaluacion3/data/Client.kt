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
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var creationDate: LocalDate = LocalDate.now()
) {
    override fun toString(): String {
        return "Client(id=$id, name='$name', birdDate=$birdDate, email='$email', phone='$phone', dniFront='$dniFront', " +
                "dniBack='$dniBack', lat=$lat, long=$lon, creationDate=$creationDate)"
    }
}