package org.simple.clinic.summary

import org.simple.clinic.drugs.PrescribedDrug

data class PatientSummaryItems(
    val prescription: List<PrescribedDrug>,
    val bloodPressureListItems: List<SummaryBloodPressureListItem>,
    val medicalHistoryItems: SummaryMedicalHistoryItem
)
