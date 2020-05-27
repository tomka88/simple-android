package org.simple.clinic.sync.indicator

import org.simple.clinic.util.ResolvedError
import org.threeten.bp.Duration

sealed class SyncIndicatorEffect

object FetchLastSyncedStatus : SyncIndicatorEffect()

object FetchDataForSyncIndicatorState : SyncIndicatorEffect()

data class StartSyncedStateTimer(val timerDuration: Duration) : SyncIndicatorEffect()

object InitiateDataSync : SyncIndicatorEffect()

data class ShowDataSyncErrorDialog(val errorType: ResolvedError) : SyncIndicatorEffect()

object FetchPendingSyncRecordCount : SyncIndicatorEffect()
