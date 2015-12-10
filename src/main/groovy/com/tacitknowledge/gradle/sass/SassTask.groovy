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
    execWithArgs("${project.projectDir}/$SCSS_PATH", 
      "-o", "${project.buildDir}/resources/main/META-INF/assets",
      "--include-path", "${project.buildDir}/scss/META-INF/assets",
      *includePath,
      *additionalParameters)

    execWithArgs("${project.buildDir}/scss/META-INF/assets", 
      "-o", "${project.buildDir}/resources/main/META-INF/assets",
      *includePath,
      *additionalParameters)
  }

  def getIncludePath()
  {
    project.sass?.includePaths?.collectMany { ['--include-path', it.path] } ?: []
  }

  def hasScss(assetsPath)
  {
    def dir = project.file(assetsPath)
    dir.exists() && dir.list().any { !it.startsWith('_') }
  }

  def execWithArgs(assetsPath, ...params)
  {
    if(hasScss(assetsPath)) 
    {
      args = [assetsPath, *params]
      super.exec()
    }
  }

  abstract def getAdditionalParameters()
}
