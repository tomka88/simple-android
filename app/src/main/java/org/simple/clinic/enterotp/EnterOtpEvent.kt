package org.simple.clinic.enterotp

import org.simple.clinic.user.User
import org.simple.clinic.widgets.UiEvent

sealed class EnterOtpEvent: UiEvent

data class UserLoaded(val user: User): EnterOtpEvent()

data class EnterOtpSubmitted(val otp: String) : EnterOtpEvent() {
  override val analyticsName = "Enter OTP Manually:OTP Submit Clicked"
}
