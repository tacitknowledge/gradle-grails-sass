package com.tacitknowledge.gradle.sass

class SassCompileTask extends SassTask
{
  @Override
  def getAdditionalParameters() { ['--output-style', 'compressed'] }
}
