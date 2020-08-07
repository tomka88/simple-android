package org.simple.clinic.activity

import com.f2prateek.rx.preferences2.Preference
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.simple.clinic.TestData
import org.simple.clinic.activity.ActivityLifecycle.Started
import org.simple.clinic.activity.ActivityLifecycle.Stopped
import org.simple.clinic.login.applock.AppLockConfig
import org.simple.clinic.main.TheActivity
import org.simple.clinic.main.TheActivityController
import org.simple.clinic.patient.PatientRepository
import org.simple.clinic.user.User
import org.simple.clinic.user.User.LoggedInStatus.LOGGED_IN
import org.simple.clinic.user.User.LoggedInStatus.NOT_LOGGED_IN
import org.simple.clinic.user.User.LoggedInStatus.OTP_REQUESTED
import org.simple.clinic.user.User.LoggedInStatus.RESETTING_PIN
import org.simple.clinic.user.User.LoggedInStatus.RESET_PIN_REQUESTED
import org.simple.clinic.user.UserSession
import org.simple.clinic.user.UserStatus
import org.simple.clinic.util.Just
import org.simple.clinic.util.Optional
import org.simple.clinic.util.RxErrorsRule
import org.simple.clinic.util.TestUtcClock
import org.simple.clinic.util.toOptional
import org.simple.clinic.widgets.UiEvent
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import java.util.concurrent.TimeUnit

class TheActivityControllerTest {

  @get:Rule
  val rxErrorsRule = RxErrorsRule()

  private val lockInMinutes = 15L

  private val activity = mock<TheActivity>()
  private val userSession = mock<UserSession>()
  private val patientRepository = mock<PatientRepository>()
  private val lockAfterTimestamp = mock<Preference<Instant>>()
  private val uiEvents = PublishSubject.create<UiEvent>()

  private val currentTimestamp = Instant.parse("2018-01-01T00:00:00Z")
  private val clock = TestUtcClock(currentTimestamp)

  private lateinit var controllerSubscription: Disposable

  @After
  fun tearDown() {
    controllerSubscription.dispose()
  }

  @Test
  fun `when activity is started and user is logged out then app lock shouldn't be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(false)

    setupController()
    uiEvents.onNext(Started(null))

    verify(activity, never()).showAppLockScreen()
  }

  @Test
  fun `when activity is started, user has requested an OTP, and user was inactive then app lock should be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    val userStream: Observable<Optional<User>> = Observable.just(Just(TestData.loggedInUser(
        uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
        loggedInStatus = OTP_REQUESTED,
        status = UserStatus.ApprovedForSyncing
    )))

    val lockAfterTime = currentTimestamp.minusSeconds(TimeUnit.MINUTES.toSeconds(1))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController(userStream = userStream)
    uiEvents.onNext(Started(null))

    verify(activity).showAppLockScreen()
  }

  @Test
  fun `when activity is started, user is logged in, and user was inactive then app lock should be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    val userStream: Observable<Optional<User>> = Observable.just(Just(TestData.loggedInUser(
        uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
        loggedInStatus = LOGGED_IN,
        status = UserStatus.ApprovedForSyncing
    )))

    val lockAfterTime = currentTimestamp.minusSeconds(TimeUnit.MINUTES.toSeconds(1))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController(userStream = userStream)
    uiEvents.onNext(Started(null))

    verify(activity).showAppLockScreen()
  }

  @Test
  fun `when activity is started, user has requested a PIN reset, and user was inactive then app lock should be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    val userStream: Observable<Optional<User>> = Observable.just(Just(TestData.loggedInUser(
        uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
        loggedInStatus = RESET_PIN_REQUESTED,
        status = UserStatus.ApprovedForSyncing
    )))

    val lockAfterTime = currentTimestamp.minusSeconds(TimeUnit.MINUTES.toSeconds(1))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController(userStream = userStream)
    uiEvents.onNext(Started(null))

    verify(activity).showAppLockScreen()
  }

  @Test
  fun `when activity is started, user is not logged in and user was inactive then app lock should not be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(userSession.loggedInUser())
        .thenReturn(Observable.just(Just(TestData.loggedInUser(
            uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
            loggedInStatus = NOT_LOGGED_IN,
            status = UserStatus.ApprovedForSyncing
        ))))

    val lockAfterTime = currentTimestamp.minusSeconds(TimeUnit.MINUTES.toSeconds(1))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController()
    uiEvents.onNext(Started(null))

    verify(activity, never()).showAppLockScreen()
  }

  @Test
  fun `when activity is started, user is resetting the PIN, and user was inactive then app lock should not be shown`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(userSession.loggedInUser())
        .thenReturn(Observable.just(Just(TestData.loggedInUser(
            uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
            loggedInStatus = RESETTING_PIN,
            status = UserStatus.ApprovedForSyncing
        ))))

    val lockAfterTime = currentTimestamp.minusSeconds(TimeUnit.MINUTES.toSeconds(1))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController()
    uiEvents.onNext(Started(null))

    verify(activity, never()).showAppLockScreen()
  }

  @Test
  fun `when app is stopped and lock timer is unset then the timer should be updated`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.MAX)
    whenever(lockAfterTimestamp.isSet).thenReturn(false)

    setupController()
    uiEvents.onNext(Stopped(null))

    verify(lockAfterTimestamp).set(currentTimestamp.plus(lockInMinutes, ChronoUnit.MINUTES))
  }

  @Test
  fun `when app is stopped and lock timer is set then the timer should not be updated`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(lockAfterTimestamp.isSet).thenReturn(true)
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.now())

    setupController()
    uiEvents.onNext(Stopped(null))

    verify(lockAfterTimestamp, never()).set(any())
  }

  @Test
  fun `when app is started unlocked and lock timer hasn't expired yet then the timer should be unset`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    val userStream: Observable<Optional<User>> = Observable.just(Just(TestData.loggedInUser(
        uuid = UUID.fromString("049ee3e0-f5a8-4ba6-9270-b20231d3fe50"),
        loggedInStatus = LOGGED_IN,
        status = UserStatus.ApprovedForSyncing
    )))

    val lockAfterTime = Instant.now().plusSeconds(TimeUnit.MINUTES.toSeconds(10))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController(userStream = userStream)
    uiEvents.onNext(Started(null))

    verify(lockAfterTimestamp).delete()
  }

  @Test
  fun `when app is started locked and lock timer hasn't expired yet then the timer should not be unset`() {
    whenever(userSession.isUserLoggedIn()).thenReturn(true)

    val lockAfterTime = Instant.now().minusSeconds(TimeUnit.MINUTES.toSeconds(5))
    whenever(lockAfterTimestamp.get()).thenReturn(lockAfterTime)

    setupController()
    uiEvents.onNext(Started(null))

    verify(lockAfterTimestamp, never()).delete()
  }

  @Test
  fun `the logged out alert must be shown only at the instant when a user gets verified for login`() {
    val user = TestData.loggedInUser(
        uuid = UUID.fromString("bed4a670-7f03-44ab-87ca-f297ca35375a"),
        status = UserStatus.ApprovedForSyncing,
        loggedInStatus = OTP_REQUESTED
    )
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.MAX)

    val userStream: Observable<Optional<User>> = Observable.just(
        Just(user),
        Just(user.copy(loggedInStatus = LOGGED_IN)),
        Just(user.copy(loggedInStatus = LOGGED_IN))
    )

    setupController(userStream = userStream)
    uiEvents.onNext(Started(null))

    verify(activity).showUserLoggedOutOnOtherDeviceAlert()
  }

  @Test
  fun `the logged out alert must not be shown if the user is already logged in when the screen is opened`() {
    val user = TestData.loggedInUser(
        uuid = UUID.fromString("bed4a670-7f03-44ab-87ca-f297ca35375a"),
        status = UserStatus.ApprovedForSyncing,
        loggedInStatus = LOGGED_IN
    )
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.MAX)
    whenever(userSession.loggedInUser()).thenReturn(
        Observable.just(
            Just(user),
            Just(user.copy(loggedInStatus = LOGGED_IN)),
            Just(user.copy(loggedInStatus = LOGGED_IN)))
    )

    setupController()
    uiEvents.onNext(Started(null))

    verify(activity, never()).showUserLoggedOutOnOtherDeviceAlert()
  }

  @Test
  fun `when user is denied access then access denied screen should show`() {
    //given
    val fullName = "Anish Acharya"
    val loggedInUser = TestData.loggedInUser(
        uuid = UUID.fromString("0b350f89-ed0e-4922-b384-7f7a9bf3aba0"),
        name = fullName,
        status = UserStatus.DisapprovedForSyncing,
        loggedInStatus = LOGGED_IN
    )
    whenever(userSession.loggedInUser()).thenReturn(Observable.just(loggedInUser.toOptional()))
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.now())
    whenever(patientRepository.clearPatientData()).thenReturn(Completable.complete())
    whenever(userSession.loggedInUserImmediate()).thenReturn(loggedInUser)
    val userDisapprovedSubject = PublishSubject.create<Boolean>()

    //when
    setupController(userDisapprovedStream = userDisapprovedSubject)
    userDisapprovedSubject.onNext(true)

    //then
    verify(patientRepository).clearPatientData()
    verify(activity).showAccessDeniedScreen(fullName)
    verifyNoMoreInteractions(activity)
  }

  @Test
  fun `when user has access then the access denied screen should not appear`() {
    //given
    val fullName = "Anish Acharya"
    val loggedInUser = TestData.loggedInUser(
        uuid = UUID.fromString("0b350f89-ed0e-4922-b384-7f7a9bf3aba0"),
        name = fullName,
        status = UserStatus.ApprovedForSyncing,
        loggedInStatus = LOGGED_IN
    )
    whenever(userSession.loggedInUser()).thenReturn(Observable.just(loggedInUser.toOptional()))
    whenever(userSession.isUserLoggedIn()).thenReturn(true)
    whenever(lockAfterTimestamp.get()).thenReturn(Instant.now())

    //when
    setupController()
    uiEvents.onNext(Started(null))

    //then
    verify(activity, never()).showAccessDeniedScreen(fullName)
    verify(patientRepository, never()).clearPatientData()
  }

  @Test
  fun `the sign in screen must be shown only at the moment where the user gets logged out`() {
    val userUnauthorizedSubject = PublishSubject.create<Boolean>()
    setupController(userUnauthorizedStream = userUnauthorizedSubject)

    userUnauthorizedSubject.onNext(false)
    verify(activity, never()).redirectToLogin()

    userUnauthorizedSubject.onNext(true)
    verify(activity).redirectToLogin()

    clearInvocations(activity)

    userUnauthorizedSubject.onNext(true)
    verifyZeroInteractions(activity)

    userUnauthorizedSubject.onNext(false)
    verifyZeroInteractions(activity)

    userUnauthorizedSubject.onNext(true)
    verify(activity).redirectToLogin()
  }

  private fun setupController(
      userStream: Observable<Optional<User>> = Observable.just(Optional.empty()),
      userUnauthorizedStream: Observable<Boolean> = Observable.just(false),
      userDisapprovedStream: Observable<Boolean> = Observable.just(false)
  ) {
    whenever(userSession.isUserUnauthorized()).thenReturn(userUnauthorizedStream)
    whenever(userSession.loggedInUser()).thenReturn(userStream)
    whenever(userSession.isUserDisapproved()).thenReturn(userDisapprovedStream)

    val controller = TheActivityController(
        userSession = userSession,
        appLockConfig = AppLockConfig(lockAfterTimeMillis = TimeUnit.MINUTES.toMillis(lockInMinutes)),
        patientRepository = patientRepository,
        utcClock = clock,
        lockAfterTimestamp = lockAfterTimestamp
    )

    controllerSubscription = uiEvents
        .compose(controller)
        .subscribe { uiChange -> uiChange(activity) }
  }
}
