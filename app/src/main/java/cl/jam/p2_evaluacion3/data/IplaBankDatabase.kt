package cl.jam.p2_evaluacion3.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cl.jam.p2_evaluacion3.converters.LocalDateConverter

@Database(entities = [Client::class], version = 1)
@TypeConverters(LocalDateConverter::class)
abstract class IplaBankDatabase: RoomDatabase() {
    abstract fun clientDao(): ClientDao
}