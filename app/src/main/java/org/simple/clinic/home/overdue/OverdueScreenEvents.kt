package org.simple.clinic.home.overdue

import org.simple.clinic.widgets.UiEvent
import java.util.UUID

class OverdueScreenCreated : UiEvent

data class CallPatientClicked(val patientUuid: UUID) : UiEvent {
  override val analyticsName = "Overdue Screen:Call Patient clicked"
}

data class OpenPatientSummaryClicked(val patientUuid: UUID) : UiEvent {
  override val analyticsName = "Overdue Screen:Open Patient summary clicked"
}
