package com.github.amaksoft.ElasticLibs

/**
 * Created by amak on 8/18/16.
 */
class SettingsUtils {
    String envVarSwitch = "JENKINS_URL" // because we use Jenkins ;)

    def isValidProjectDir(String pathToDir) {
        new File(pathToDir, "build.gradle").exists()
    }

    boolean checkEnv() {
        System.getenv(envVarSwitch) == null
    }

    def includeIfValid(String projectName, String pathToDir) {
        if (isValidProjectDir(pathToDir) && checkEnv()) {
            include projectName
            project(projectName).projectDir = new File(pathToDir)
            println "Including local project \"" + projectName + "\" (\"" + pathToDir + "\")"
        } else if (!isValidProjectDir(pathToDir)){
            println "Not including local project \"" + projectName + "\" (\"" + pathToDir + "\"). Local project doesn't exist."
        } else {
            println "Not including local project \"" + projectName + "\" (\"" + pathToDir + "\"). Cancelled by with environment variable \"$envVarSwitch\""
        }
    }
}
