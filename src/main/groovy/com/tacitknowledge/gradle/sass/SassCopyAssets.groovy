package com.tacitknowledge.gradle.sass

import org.gradle.api.tasks.Copy
import org.gradle.api.file.FileCopyDetails
import org.gradle.api.file.RelativePath

class SassCopyAssets extends Copy
{
  SassCopyAssets()
  {
    project.afterEvaluate{
      //copying scss assets from current project
      from("${project.projectDir}/grails-app/assets/scss") {
        include '**/*'
        eachFile { FileCopyDetails fcp ->
            fcp.relativePath = new RelativePath(true, 'META-INF','assets', *fcp.relativePath.segments)
        }
      }
      //copying scss assets from dependencies
      from project.configurations.compile.collect {
        project.zipTree(it).matching {
          include '**/META-INF/assets/**/*.scss'
        }
      }

      into "$project.buildDir/scss"
    }
  }
}
