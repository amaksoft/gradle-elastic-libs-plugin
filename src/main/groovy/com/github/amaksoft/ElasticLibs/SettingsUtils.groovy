package com.github.amaksoft.ElasticLibs

/**
 * Created by amak on 8/18/16.
 */
class SettingsUtils {

    def isValidProjectDir(String pathToDir) {
        new File(pathToDir, "build.gradle").exists()
    }

    def includeIfValid(String projectName, String pathToDir) {
        if (isValidProjectDir(pathToDir)) {
            include projectName
            project(projectName).projectDir = new File(pathToDir)
            println "Including local project \"" + projectName + "\" (\"" + pathToDir + "\")"
        } else {
            println "Not including local project \"" + projectName + "\" (\"" + pathToDir + "\"). Local project doesn't exist."
        }
    }
}
