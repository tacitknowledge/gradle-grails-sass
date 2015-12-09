package com.tacitknowledge.gradle.sass

import com.moowork.gradle.node.task.NodeTask

abstract class SassTask extends NodeTask
{
  static final String SCSS_PATH = 'grails-app/assets/scss'

  SassTask()
  {
    project.afterEvaluate {
      workingDir = project.projectDir
      script = new File(project.node.nodeModulesDir, 'node_modules/node-sass/bin/node-sass')

      inputs.dir "${project.projectDir}/$SCSS_PATH"
    }
  }

  @Override
  void exec()
  {
    if(project.file("${project.projectDir}/$SCSS_PATH").exists())
    {
      args = [
              "${project.projectDir}/$SCSS_PATH", "-o", "${project.buildDir}/resources/main/META-INF/assets",
              "--include-path", "${project.buildDir}/resource/main/META-INF/assets"
      ] + includePath + additionalParameters
      super.exec()
    }
    if(project.file("${project.buildDir}/scss/META-INF/assets").exists()) {
      args = [
              "${project.buildDir}/scss/META-INF/assets", "-o", "${project.buildDir}/resources/main/META-INF/assets"
      ] + includePath + additionalParameters
      super.exec()
    }
  }

  def getIncludePath()
  {
    project.sass?.includePaths?.collectMany { ['--include-path', it.path] } ?: []
  }

  abstract def getAdditionalParameters()
}
