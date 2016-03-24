package com.tacitknowledge.gradle.sass

class SassCompileTask extends SassTask
{
  @Override
  def getAdditionalParameters() { ['--source-map-embed', '--output-style', 'nested'] }
}
