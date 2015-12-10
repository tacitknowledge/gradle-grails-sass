package com.tacitknowledge.gradle.sass

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskAction

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
