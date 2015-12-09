package com.tacitknowledge.gradle.sass

import com.moowork.gradle.node.NodePlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.bundling.Jar

class SassGrailsPlugin implements Plugin<Project>
{
  void apply(Project project)
  {
    project.plugins.apply(NodePlugin)

    project.extensions.create('sass', SassGrailsExtension)

    project.tasks.with {
      def installSass = create(
              name: 'installSass',
              type: InstallSass)

      def copySassAssets = create(name:'copySassAssets', type: SassCopyAssets)

      def sassCompile = create(
              name: 'sassCompile',
              type: SassCompileTask,
              dependsOn: [installSass, copySassAssets])

      def sassWatch = create(
              name: 'sassWatch',
              type: SassWatchTask,
              dependsOn: [installSass, copySassAssets])

      withType(Jar) { bundleTask ->
        def inputFiles = project.fileTree(dir: "${project.projectDir}/grails-app/assets/scss", include: '**/*')
        bundleTask.inputs.files inputFiles
        bundleTask.from inputFiles, {
          into "META-INF/assets"
        }
      }
    }
  }
}
