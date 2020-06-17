package org.simple.clinic.storage.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.simple.clinic.storage.inTransaction
import javax.inject.Inject

class Migration_68 @Inject constructor(): Migration(67, 68) {

  override fun migrate(database: SupportSQLiteDatabase) {
    database.inTransaction {
      execSQL("CREATE INDEX IF NOT EXISTS `index_Patient_createdAt` ON `Patient` (`createdAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Patient_updatedAt` ON `Patient` (`updatedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Patient_deletedAt` ON `Patient` (`deletedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Patient_recordedAt` ON `Patient` (`recordedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Patient_status` ON `Patient` (`status`)")

      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodPressureMeasurement_createdAt` ON `BloodPressureMeasurement` (`createdAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodPressureMeasurement_updatedAt` ON `BloodPressureMeasurement` (`updatedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodPressureMeasurement_deletedAt` ON `BloodPressureMeasurement` (`deletedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodPressureMeasurement_recordedAt` ON `BloodPressureMeasurement` (`recordedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodPressureMeasurement_facilityUuid` ON `BloodPressureMeasurement` (`facilityUuid`)")

      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_patientUuid` ON `BloodSugarMeasurements` (`patientUuid`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_createdAt` ON `BloodSugarMeasurements` (`createdAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_updatedAt` ON `BloodSugarMeasurements` (`updatedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_deletedAt` ON `BloodSugarMeasurements` (`deletedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_recordedAt` ON `BloodSugarMeasurements` (`recordedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_BloodSugarMeasurements_facilityUuid` ON `BloodSugarMeasurements` (`facilityUuid`)")

      execSQL("CREATE INDEX IF NOT EXISTS `index_PrescribedDrug_createdAt` ON `PrescribedDrug` (`createdAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_PrescribedDrug_updatedAt` ON `PrescribedDrug` (`updatedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_PrescribedDrug_deletedAt` ON `PrescribedDrug` (`deletedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_PrescribedDrug_facilityUuid` ON `PrescribedDrug` (`facilityUuid`)")

      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_patientUuid` ON `Appointment` (`patientUuid`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_createdAt` ON `Appointment` (`createdAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_updatedAt` ON `Appointment` (`updatedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_deletedAt` ON `Appointment` (`deletedAt`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_facilityUuid` ON `Appointment` (`facilityUuid`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_creationFacilityUuid` ON `Appointment` (`creationFacilityUuid`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_status` ON `Appointment` (`status`)")
      execSQL("CREATE INDEX IF NOT EXISTS `index_Appointment_appointmentType` ON `Appointment` (`appointmentType`)")
    }
  }
}
