package org.simple.clinic.user

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.database.sqlite.SQLiteDatabase
import io.reactivex.Flowable
import org.simple.clinic.facility.Facility
import java.util.UUID

@Entity(
    tableName = "LoggedInUserFacilityMapping",
    foreignKeys = [
      ForeignKey(
          entity = LoggedInUser::class,
          parentColumns = ["uuid"],
          childColumns = ["userUuid"]),
      ForeignKey(
          entity = Facility::class,
          parentColumns = ["uuid"],
          childColumns = ["facilityUuid"])
    ],
    primaryKeys = ["userUuid", "facilityUuid"],
    indices = [(Index("facilityUuid"))])  // userUuid gets an implicit index.
data class LoggedInUserFacilityMapping(

    val userUuid: UUID,

    val facilityUuid: UUID,

    val isCurrentFacility: Boolean
) {

  @Dao
  abstract class RoomDao {

    @Transaction
    open fun insertOrUpdate(user: LoggedInUser, facilityIds: List<UUID>, newCurrentFacilityUuid: UUID) {
      if ((newCurrentFacilityUuid in facilityIds).not()) {
        throw AssertionError()
      }

      val mappings = facilityIds
          .map {
            LoggedInUserFacilityMapping(
                userUuid = user.uuid,
                facilityUuid = it,
                isCurrentFacility = it == newCurrentFacilityUuid)
          }
      insertOrUpdate(mappings)
      changeCurrentFacility(user.uuid, newCurrentFacilityUuid)
    }

    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    abstract fun insertOrUpdate(mappings: List<LoggedInUserFacilityMapping>)

    @Transaction
    open fun changeCurrentFacility(userUuid: UUID, newCurrentFacilityUuid: UUID) {
      val oldCurrentFacilityUuid = currentFacilityUuid(userUuid)
      if (oldCurrentFacilityUuid != null) {
        setFacilityIsCurrent(userUuid, oldCurrentFacilityUuid, isCurrent = false)
      }
      setFacilityIsCurrent(userUuid, newCurrentFacilityUuid, isCurrent = true)
    }

    // TODO: Test that a user only has one facility with isCurrentFacility=true.
    @Query("""
      UPDATE LoggedInUserFacilityMapping
      SET isCurrentFacility = :isCurrent
      WHERE userUuid = :userUuid AND facilityUuid = :facilityUuid
      """)
    protected abstract fun setFacilityIsCurrent(userUuid: UUID, facilityUuid: UUID, isCurrent: Boolean)

    @Query("""
      SELECT *
      FROM Facility
      INNER JOIN LoggedInUserFacilityMapping ON LoggedInUserFacilityMapping.facilityUuid = Facility.uuid
      WHERE LoggedInUserFacilityMapping.userUuid = :userUuid
      AND LoggedInUserFacilityMapping.isCurrentFacility = 1
      LIMIT 1
      """)
    abstract fun currentFacility(userUuid: UUID): Flowable<Facility>

    @Query("""
      SELECT facilityUuid FROM LoggedInUserFacilityMapping
      WHERE userUuid = :userUuid
      AND isCurrentFacility = 1
    """)
    protected abstract fun currentFacilityUuid(userUuid: UUID): UUID?

    @Query("SELECT facilityUuid FROM LoggedInUserFacilityMapping WHERE userUuid = :userUuid")
    abstract fun facilityUuids(userUuid: UUID): Flowable<List<UUID>>

    @Query("SELECT * FROM LoggedInUserFacilityMapping WHERE userUuid = :userUuid")
    abstract fun mappingsForUser(userUuid: UUID): Flowable<List<LoggedInUserFacilityMapping>>
  }
}
