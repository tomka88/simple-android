package org.simple.clinic.plugin.gradle

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class InstrumentRoomPlugin : Plugin<Project> {

  override fun apply(target: Project) {
    val extension = target.extensions.create("instrumentRoom", InstrumentRoomExtension::class.java)

    val androidPlugin = target.extensions.findByName("android") as BaseExtension
    androidPlugin.registerTransform(InstrumentRoomTransform(target, androidPlugin, extension))
  }
}

open class InstrumentRoomExtension {
  var applyFor: Array<String> = emptyArray()
}
