package androidx.room

import android.annotation.SuppressLint
import com.google.firebase.perf.FirebasePerformance
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
class PatientDaoWithTracing(
    private val database: AppDatabase
) : Patient.RoomDao() {

  override fun allPatients(): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "allPatients")
      trace.start()
      val _result = database.patientDao().allPatients().blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun getOne(uuid: UUID): Patient? {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "getOne")
    trace.start()
    val _result = database.patientDao().getOne(uuid)
    trace.stop()
    return _result
  }

  override fun patient(uuid: UUID): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "patient")
      trace.start()
      val _result = database.patientDao().patient(uuid).blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun updateSyncStatus(oldStatus: SyncStatus, newStatus: SyncStatus) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "updateSyncStatus")
    trace.start()
    database.patientDao().updateSyncStatus(oldStatus, newStatus)
    trace.stop()
  }

  override fun updateSyncStatus(uuids: List<UUID>, newStatus: SyncStatus) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "updateSyncStatus")
    trace.start()
    database.patientDao().updateSyncStatus(uuids, newStatus)
    trace.stop()
  }

  override fun patientCount(): Flowable<Int> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "patientCount")
      trace.start()
      val _result =  database.patientDao().patientCount().blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun patientCount(syncStatus: SyncStatus): Flowable<Int> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "patientCount")
      trace.start()
      val _result = database.patientDao().patientCount(syncStatus).blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun clear() {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "clear")
    trace.start()
    database.patientDao().clear()
    trace.stop()
  }

  override fun findPatientsWithBusinessId(identifier: String): Flowable<List<Patient>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "findPatientsWithBusinessId")
      trace.start()
      val _result = database.patientDao().findPatientsWithBusinessId(identifier).blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun updatePatientStatus(uuid: UUID, newStatus: PatientStatus, newSyncStatus: SyncStatus, newUpdatedAt: Instant) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "updatePatientStatus")
    trace.start()
    database.patientDao().updatePatientStatus(uuid, newStatus, newSyncStatus, newUpdatedAt)
    trace.stop()
  }

  override fun compareAndUpdateRecordedAt(patientUuid: UUID, instantToCompare: Instant, updatedAt: Instant, pendingStatus: SyncStatus) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "compareAndUpdateRecordedAt")
    trace.start()
    database.patientDao().compareAndUpdateRecordedAt(patientUuid, instantToCompare, updatedAt, pendingStatus)
    trace.stop()
  }

  override fun updateRecordedAt(patientUuid: UUID, updatedAt: Instant, pendingStatus: SyncStatus) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "updateRecordedAt")
    trace.start()
    database.patientDao().updateRecordedAt(patientUuid, updatedAt, pendingStatus)
    trace.stop()
  }

  override fun loadPatientQueryModelsWithSyncStatus(syncStatus: SyncStatus): Flowable<List<PatientQueryModel>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "loadPatientQueryModelsWithSyncStatus")
      trace.start()
      val _result = emptyList<PatientQueryModel>()
      trace.stop()
      _result
    })
  }

  override fun loadPatientQueryModelsForPatientUuid(patientUuid: UUID): Flowable<List<PatientQueryModel>> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "loadPatientQueryModelsForPatientUuid")
      trace.start()
      val _result = emptyList<PatientQueryModel>()
      trace.stop()
      _result
    })
  }

  override fun loadPatientQueryModelsForPatientUuidImmediate(patientUuid: UUID): List<PatientQueryModel> {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "loadPatientQueryModelsForPatientUuidImmediate")
    trace.start()
    val _result = emptyList<PatientQueryModel>()
    trace.stop()
    return _result
  }

  override fun isPatientDefaulter(patientUuid: UUID, yesAnswer: Answer, scheduled: Appointment.Status): Flowable<Boolean> {
    return RxRoom.createFlowable(database, false, arrayOf("Patient"), Callable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "isPatientDefaulter")
      trace.start()
      val _result = database.patientDao().isPatientDefaulter(patientUuid, yesAnswer, scheduled).blockingFirst()
      trace.stop()
      _result
    })
  }

  override fun hasPatientChangedSince(patientUuid: UUID, instantToCompare: Instant, pendingStatus: SyncStatus): Boolean {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "hasPatientChangedSince")
    trace.start()
    val _result = database.patientDao().hasPatientChangedSince(patientUuid, instantToCompare, pendingStatus)
    trace.stop()
    return _result
  }

  override fun insert(record: List<Patient>): List<Long> {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "insert")
    trace.start()
    val _result = database.patientDao().insert(record)
    trace.stop()
    return _result
  }

  override fun update(entities: List<Patient>) {
    val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
    trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
    trace.putAttribute("method", "update")
    trace.start()
    Timber.tag("WTF").d("Do nothing here")
    trace.stop()
  }

  override fun updateRecordedAt2(patientUuid: UUID, updatedAt: Instant, pendingStatus: SyncStatus): Completable {
    return Completable.fromCallable {
      val trace = FirebasePerformance.getInstance().newTrace("RoomDb")
      trace.putAttribute("dao", "androidx.room.PatientDaoWithTracing")
      trace.putAttribute("method", "updateRecordedAt2")
      trace.start()
      Timber.tag("WTF").d("Do nothing here")
      trace.stop()
    }
  }
}
