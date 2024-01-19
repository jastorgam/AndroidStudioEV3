package cl.jam.p2_evaluacion3

import android.app.Application
import androidx.room.Room
import cl.jam.p2_evaluacion3.data.IplaBankDatabase

class IplaBankApplication : Application() {
    val db by lazy {
        Room.databaseBuilder(this, IplaBankDatabase::class.java, "iplabank.db").build()
    }
    val clientDao by lazy { db.clientDao() }
}