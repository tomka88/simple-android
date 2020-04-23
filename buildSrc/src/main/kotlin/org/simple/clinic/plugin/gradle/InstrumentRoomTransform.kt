package org.simple.clinic.plugin.gradle

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

class InstrumentRoomTransform(
    project: Project,
    private val androidPlugin: BaseExtension,
    private var extension: InstrumentRoomExtension
) : Transform() {

  private val logger = project.logger

  override fun getName(): String = "InstrumentRoom"

  override fun getInputTypes(): Set<QualifiedContent.ContentType> {
    return setOf(QualifiedContent.DefaultContentType.CLASSES)
  }

  override fun isIncremental(): Boolean = false

  override fun getScopes(): MutableSet<in QualifiedContent.Scope>? {
    return mutableSetOf(QualifiedContent.Scope.PROJECT)
  }

  override fun transform(transformInvocation: TransformInvocation) {
    if(logger.isLifecycleEnabled) {
      logger.lifecycle("TRANSFORM!")
    }
    super.transform(transformInvocation)
  }
}
