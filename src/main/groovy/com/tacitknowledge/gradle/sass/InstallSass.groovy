package com.tacitknowledge.gradle.sass

import com.moowork.gradle.node.task.NpmTask

class InstallSass extends NpmTask
{
  InstallSass()
  {
    group = 'Sass'
    description = 'Installs sass bin (node) into project'

    project.afterEvaluate{
      workingDir = project.node.nodeModulesDir
      
      def pkgName = project.sass.version ? "node-sass@${project.sass.version}" : 'node-sass'
      args = ['install', pkgName]

      outputs.dir new File(project.node.nodeModulesDir, "node_modules/node-sass")
    }
  }
}
