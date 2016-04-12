package com.tacitknowledge.gradle.sass

class SassCompileTask extends SassTask
{
  @Override
  def getAdditionalParameters() {
    project.sass.additionalParameters ?: ['--output-style','compressed']
  }
}
