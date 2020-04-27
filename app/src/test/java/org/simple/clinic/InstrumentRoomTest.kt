package org.simple.clinic

import androidx.room.InsnInfo
import androidx.room.RoomInstrumentation
import androidx.room.toClassNode
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class InstrumentRoomTest {

  @Test
  fun `verify bytecode is generated`() {
    val folder = File("src/test/resources/room-asm")
    val classFileWithoutTracing = File(folder, "PatientDaoWithoutTracing.class")
    val classFileWithTracing = File(folder, "PatientDaoWithTracing.class")
    val tracingMethodsFile = File(folder, "TracingMethods.class")

    val sourceClass = classFileWithoutTracing.toClassNode()
    val targetClass = classFileWithTracing.toClassNode()

    val roomInstrumentation = RoomInstrumentation(tracingMethodsFile) { File(folder, it).toClassNode() }
    roomInstrumentation.instrument(
        sourceClass = sourceClass,
        daoName = "org.simple.clinic.Patient.RoomDao"
    )

    val instructionsInSource = sourceClass.methods.first { it.name == "updateRecordedAt2" }.instructions
    val instructionsInTarget = sourceClass.methods.first { it.name == "updateRecordedAt2" }.instructions

    val sourceInsnInfo = InsnInfo.from(instructionsInSource)
    val targetInsnInfo = InsnInfo.from(instructionsInTarget)

    assertThat(sourceClass).isEqualTo(targetClass)
  }
}
