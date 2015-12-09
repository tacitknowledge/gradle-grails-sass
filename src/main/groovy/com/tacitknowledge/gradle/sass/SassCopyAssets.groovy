package com.tacitknowledge.gradle.sass

import com.sun.javafx.scene.CameraHelper
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction

/**
 * Created by ifrolov on 9/12/2015.
 */
class SassCopyAssets extends Copy
{
  SassCopyAssets()
  {
    project.afterEvaluate{
      from project.configurations.compile.collect{ project.zipTree(it).matching { include '**/META-INF/assets/**/*.scss' } }
      into "$project.buildDir/scss"
    }
  }
}
