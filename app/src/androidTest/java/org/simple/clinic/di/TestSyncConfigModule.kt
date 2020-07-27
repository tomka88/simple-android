package org.simple.clinic.di

import dagger.Module
import dagger.Provides
import org.simple.clinic.sync.BatchSize
import org.simple.clinic.sync.SyncConfig
import org.simple.clinic.sync.SyncGroup
import org.simple.clinic.sync.SyncInterval
import org.simple.clinic.sync.SyncModuleConfig
import javax.inject.Named

@Module
class TestSyncConfigModule {

  @Provides
  @Named("sync_config_frequent")
  fun frequentSyncConfig(syncModuleConfig: SyncModuleConfig): SyncConfig {
    return SyncConfig(
        syncInterval = SyncInterval.FREQUENT,
        batchSize = BatchSize.VERY_SMALL,
        syncGroup = SyncGroup.FREQUENT
    )
  }

  @Provides
  @Named("sync_config_daily")
  fun dailySyncConfig(syncModuleConfig: SyncModuleConfig): SyncConfig {
    return SyncConfig(
        syncInterval = SyncInterval.DAILY,
        batchSize = BatchSize.VERY_SMALL,
        syncGroup = SyncGroup.DAILY
    )
  }
}
