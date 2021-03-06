package org.simple.clinic.bloodsugar.entry.di

import dagger.Subcomponent
import org.simple.clinic.activity.BindsActivity
import org.simple.clinic.bloodsugar.entry.BloodSugarEntrySheet
import org.simple.clinic.bloodsugar.entry.confirmremovebloodsugar.ConfirmRemoveBloodSugarDialogInjector
import org.simple.clinic.di.AssistedInjectModule

@Subcomponent(modules = [AssistedInjectModule::class])
interface BloodSugarEntryComponent : ConfirmRemoveBloodSugarDialogInjector {

  fun inject(target: BloodSugarEntrySheet)

  @Subcomponent.Builder
  interface Builder : BindsActivity<Builder> {

    fun build(): BloodSugarEntryComponent
  }
}
