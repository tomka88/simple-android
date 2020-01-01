package org.simple.clinic.bloodsugar.entry

import com.spotify.mobius.Next
import com.spotify.mobius.Update
import org.simple.clinic.bloodsugar.entry.BloodSugarEntrySheet.ScreenType.BLOOD_SUGAR_ENTRY
import org.simple.clinic.bloodsugar.entry.BloodSugarEntrySheet.ScreenType.DATE_ENTRY
import org.simple.clinic.bloodsugar.entry.BloodSugarValidator.Result.Valid
import org.simple.clinic.mobius.dispatch
import org.simple.clinic.mobius.next
import org.simple.clinic.util.UserInputDatePaddingCharacter
import org.simple.clinic.widgets.ageanddateofbirth.UserInputDateValidator
import org.simple.clinic.widgets.ageanddateofbirth.UserInputDateValidator.Result
import org.threeten.bp.LocalDate

class BloodSugarEntryUpdate(
    private val bloodSugarValidator: BloodSugarValidator,
    private val dateValidator: UserInputDateValidator,
    private val dateInUserTimeZone: LocalDate,
    private val inputDatePaddingCharacter: UserInputDatePaddingCharacter
) : Update<BloodSugarEntryModel, BloodSugarEntryEvent, BloodSugarEntryEffect> {

  override fun update(
      model: BloodSugarEntryModel,
      event: BloodSugarEntryEvent
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> {
    return when (event) {
      is ScreenChanged -> next(model.screenChanged(event.type))
      is DatePrefilled -> next(model.datePrefilled(event.prefilledDate))
      is BloodSugarChanged -> next(model.bloodSugarChanged(event.bloodSugarReading), HideBloodSugarErrorMessage)
      is DayChanged -> onDateChanged(model.dayChanged(event.day))
      is MonthChanged -> onDateChanged(model.monthChanged(event.month))
      is YearChanged -> onDateChanged(model.yearChanged(event.twoDigitYear))
      BackPressed -> onBackPressed(model)
      BloodSugarDateClicked -> onBloodSugarDateClicked(model)
      ShowBloodSugarEntryClicked -> showBloodSugarClicked(model)
      SaveClicked -> onSaveClicked(model)
      is BloodSugarSaved -> dispatch(SetBloodSugarSavedResultAndFinish)
    }
  }

  private fun onSaveClicked(
      model: BloodSugarEntryModel
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> {
    val bloodSugarValidationResult = bloodSugarValidator.validate(model.bloodSugarReading)
    val dateValidationResult = dateValidator.validate(getDateText(model), dateInUserTimeZone)
    val validationErrorEffects = getValidationErrorEffects(bloodSugarValidationResult, dateValidationResult)

    return if (validationErrorEffects.isNotEmpty()) {
      Next.dispatch(validationErrorEffects)
    } else {
      dispatch(getCreateEntryEffect(model, dateValidationResult))
    }
  }

  private fun getValidationErrorEffects(
      bloodSugarValidationResult: BloodSugarValidator.Result,
      dateValidationResult: Result
  ): Set<BloodSugarEntryEffect> {
    val validationErrorEffects = mutableSetOf<BloodSugarEntryEffect>()

    if (bloodSugarValidationResult !is Valid) {
      validationErrorEffects.add(ShowBloodSugarValidationError(bloodSugarValidationResult))
    }

    if (dateValidationResult !is Result.Valid) {
      validationErrorEffects.add(ShowDateValidationError(dateValidationResult))
    }
    return validationErrorEffects.toSet()
  }

  private fun getCreateEntryEffect(
      model: BloodSugarEntryModel,
      dateValidationResult: Result
  ): BloodSugarEntryEffect {
    val openAs = model.openAs as New
    val bloodSugarReading = model.bloodSugarReading.toInt()
    val bloodSugarMeasurementType = openAs.measurementType
    val userEnteredDate = (dateValidationResult as Result.Valid).parsedDate
    val prefillDate = model.prefilledDate!!

    return CreateNewBloodSugarEntry(
        openAs.patientId,
        bloodSugarReading,
        bloodSugarMeasurementType,
        userEnteredDate,
        prefillDate
    )
  }

  private fun showBloodSugarClicked(
      model: BloodSugarEntryModel
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> {
    val result = dateValidator.validate(getDateText(model), dateInUserTimeZone)
    val effect = if (result is Result.Valid) {
      ShowBloodSugarEntryScreen(result.parsedDate)
    } else {
      ShowDateValidationError(result)
    }
    return dispatch(effect)
  }

  private fun onBloodSugarDateClicked(
      model: BloodSugarEntryModel
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> {
    val result = bloodSugarValidator.validate(model.bloodSugarReading)
    val effect = if (result is Valid) {
      ShowDateEntryScreen
    } else {
      ShowBloodSugarValidationError(result)
    }
    return dispatch(effect)
  }

  private fun onBackPressed(
      model: BloodSugarEntryModel
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> {
    return when (model.activeScreen) {
      BLOOD_SUGAR_ENTRY -> dispatch(Dismiss as BloodSugarEntryEffect)
      DATE_ENTRY -> showBloodSugarClicked(model)
    }
  }

  private fun onDateChanged(
      updatedModel: BloodSugarEntryModel
  ): Next<BloodSugarEntryModel, BloodSugarEntryEffect> =
      next(updatedModel, HideDateErrorMessage)

  private fun getDateText(model: BloodSugarEntryModel) =
      formatToPaddedDate(model.day, model.month, model.twoDigitYear, model.year)

  private fun formatToPaddedDate(day: String, month: String, twoDigitYear: String, fourDigitYear: String): String {
    val paddedDd = day.padStart(length = 2, padChar = inputDatePaddingCharacter.value)
    val paddedMm = month.padStart(length = 2, padChar = inputDatePaddingCharacter.value)
    val paddedYy = twoDigitYear.padStart(length = 2, padChar = inputDatePaddingCharacter.value)

    val firstTwoDigitsOfYear = fourDigitYear.substring(0, 2)
    val paddedYyyy = firstTwoDigitsOfYear + paddedYy
    return "$paddedDd/$paddedMm/$paddedYyyy"
  }
}