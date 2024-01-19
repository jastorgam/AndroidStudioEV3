package cl.jam.p2_evaluacion3.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClientDao {
    @Query("SELECT * FROM client ORDER BY birdDate DESC")
    suspend fun getAll(): List<Client>

    @Query("SELECT * FROM client WHERE id = :id")
    suspend fun getById(id: Long): Client

    @Insert
    suspend fun insert(client: Client)

    @Update
    suspend fun update(client: Client)

    @Delete
    suspend fun delete(client: Client)
}