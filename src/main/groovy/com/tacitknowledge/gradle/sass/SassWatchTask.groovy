package com.tacitknowledge.gradle.sass

class SassWatchTask extends SassTask
{
  SassWatchTask()
  {
    ignoreExitValue = true
  }

  @Override
  void exec()
  {
    Thread.start {
      super.exec()
    }
  }

  @Override
  def getAdditionalParameters() { ['-w', '-r'] }
}
