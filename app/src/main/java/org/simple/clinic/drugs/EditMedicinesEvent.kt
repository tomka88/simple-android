package org.simple.clinic.drugs

import org.simple.clinic.widgets.UiEvent

sealed class EditMedicinesEvent : UiEvent

object AddNewPrescriptionClicked : EditMedicinesEvent() {
  override val analyticsName = "Drugs:Protocol:Add Custom Clicked"
}

data class ProtocolDrugClicked(
    val drugName: String,
    val prescriptionForProtocolDrug: PrescribedDrug?
) : EditMedicinesEvent() {
  override val analyticsName = "Drugs:Protocol:Selected"
}

data class CustomPrescriptionClicked(val prescribedDrug: PrescribedDrug) : EditMedicinesEvent() {
  override val analyticsName = "Drugs:Protocol:Edit CustomPrescription Clicked"
}

