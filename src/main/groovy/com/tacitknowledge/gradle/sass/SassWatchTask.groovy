package com.tacitknowledge.gradle.sass

import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.StandardCopyOption
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchKey
import java.nio.file.WatchService
import java.nio.file.attribute.BasicFileAttributes

class SassWatchTask extends SassTask
{

  WatchService watcher
  Map<WatchKey, Path> keys

  SassWatchTask() {
    ignoreExitValue = true
    this.watcher = FileSystems.getDefault().newWatchService();
    this.keys = new HashMap<WatchKey, Path>();
  }

  void registerRecursive(Path start) throws IOException {
    Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
      @Override
      FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        registerDir(dir);
        return FileVisitResult.CONTINUE;
      }
    });
  }

  private void registerDir(Path path) {
    println "Registering $path to watch for changes"
    WatchKey key = path.register(this.watcher,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY);
    keys.put(key, path);
  }

  @Override
  void exec() {
    Thread.start {
      project.sass.watchList.each { String dir ->
        Path path = Paths.get(dir)
        registerRecursive(path)
      }

      for (;;) {
        WatchKey key;
        try {
            key = watcher.take();
        } catch (InterruptedException x) {
            return;
        }
        //while (true) {
        key.pollEvents().each { event ->
          //copy only .scss files
          if (event.context().toString().contains('.scss')) {
            Path location = keys.get(key); //get the full path of the file to copy
            if (location) {
              Path src = location.resolve(event.context())
              String dest = "$project.buildDir/scss/META-INF/assets/${src.toString().split('/scss/').last()}"
              println "copying $src to $dest"
              Files.copy(src, Paths.get(dest), StandardCopyOption.REPLACE_EXISTING)
            }
          }
        }
        boolean valid = key.reset();
        if (!valid) {
            break;
        }
      }
    }

    Thread.start('sassWatchThread') {
      super.exec()
    }
  }

  @Override
  def getAdditionalParameters() {
    project.sass.additionalParameters ? project.sass.additionalParameters + ['-w', '-r'] : ['-w', '-r']
  }
}
