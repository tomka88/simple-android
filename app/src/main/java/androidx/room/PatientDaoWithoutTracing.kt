package androidx.room

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Flowable
import org.simple.clinic.AppDatabase
import org.simple.clinic.medicalhistory.Answer
import org.simple.clinic.overdue.Appointment
import org.simple.clinic.patient.Patient
import org.simple.clinic.patient.PatientStatus
import org.simple.clinic.patient.SyncStatus
import org.threeten.bp.Instant
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.Callable

@SuppressLint("RestrictedApi")
class PatientDaoWithoutTracing(
    private val database: AppDatabase
) : Patient.RoomDao() {

  override fun allPatients(): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().allPatients().blockingFirst()
    })
  }

  override fun getOne(uuid: UUID): Patient? {
    return database.patientDao().getOne(uuid)
  }

  override fun patient(uuid: UUID): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().patient(uuid).blockingFirst()
    })
  }

  override fun updateSyncStatus(oldStatus: SyncStatus, newStatus: SyncStatus) {
    database.patientDao().updateSyncStatus(oldStatus, newStatus)
  }

  override fun updateSyncStatus(uuids: List<UUID>, newStatus: SyncStatus) {
    database.patientDao().updateSyncStatus(uuids, newStatus)
  }

  override fun patientCount(): Flowable<Int> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().patientCount().blockingFirst()
    })
  }

  override fun patientCount(syncStatus: SyncStatus): Flowable<Int> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().patientCount(syncStatus).blockingFirst()
    })
  }

  override fun clear() {
    database.patientDao().clear()
  }

  override fun findPatientsWithBusinessId(identifier: String): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().findPatientsWithBusinessId(identifier).blockingFirst()
    })
  }

  override fun updatePatientStatus(uuid: UUID, newStatus: PatientStatus, newSyncStatus: SyncStatus, newUpdatedAt: Instant) {
    database.patientDao().updatePatientStatus(uuid, newStatus, newSyncStatus, newUpdatedAt)
  }

  override fun compareAndUpdateRecordedAt(patientUuid: UUID, instantToCompare: Instant, updatedAt: Instant, pendingStatus: SyncStatus) {
    database.patientDao().compareAndUpdateRecordedAt(patientUuid, instantToCompare, updatedAt, pendingStatus)
  }

  override fun updateRecordedAt(patientUuid: UUID, updatedAt: Instant, pendingStatus: SyncStatus) {
    database.patientDao().updateRecordedAt(patientUuid, updatedAt, pendingStatus)
  }

  override fun loadPatientQueryModelsWithSyncStatus(syncStatus: SyncStatus): Flowable<List<PatientQueryModel>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      emptyList<PatientQueryModel>()
    })
  }

  override fun loadPatientQueryModelsForPatientUuid(patientUuid: UUID): Flowable<List<PatientQueryModel>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      emptyList<PatientQueryModel>()
    })
  }

  override fun loadPatientQueryModelsForPatientUuidImmediate(patientUuid: UUID): List<PatientQueryModel> {
    return emptyList<PatientQueryModel>()
  }

  override fun isPatientDefaulter(patientUuid: UUID, yesAnswer: Answer, scheduled: Appointment.Status): Flowable<Boolean> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      database.patientDao().isPatientDefaulter(patientUuid, yesAnswer, scheduled).blockingFirst()
    })
  }

  override fun hasPatientChangedSince(patientUuid: UUID, instantToCompare: Instant, pendingStatus: SyncStatus): Boolean {
    return database.patientDao().hasPatientChangedSince(patientUuid, instantToCompare, pendingStatus)
  }

  override fun insert(record: List<Patient>): List<Long> {
    return database.patientDao().insert(record)
  }

  override fun update(entities: List<Patient>) {
    Timber.tag("WTF").d("Do nothing here")
  }

  override fun updateRecordedAt2(patientUuid: UUID, updatedAt: Instant, pendingStatus: SyncStatus): Completable {
    return Completable.fromCallable {
      Timber.tag("WTF").d("Do nothing here")
    }
  }
}
