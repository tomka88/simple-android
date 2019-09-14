package experiments.instantsearch

import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.simple.clinic.R
import org.simple.clinic.router.screen.FullScreenKey
import org.threeten.bp.Instant
import java.util.UUID

@Parcelize
class PatientSearchScreenKey(
    val timestamp: Instant,
    val uuid: UUID
) : FullScreenKey {

  @IgnoredOnParcel
  override val analyticsName = "Patient Search:Instant Search Experiment"

  override fun layoutRes(): Int {
    return R.layout.experiment_screen_patient_search
  }
}
