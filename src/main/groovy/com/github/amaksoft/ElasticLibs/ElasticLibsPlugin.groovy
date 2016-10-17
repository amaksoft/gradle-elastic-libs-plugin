package com.github.amaksoft.ElasticLibs

/**
 * Created by amak on 8/18/16.
 */
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

class ElasticLibsPlugin implements Plugin {

    Settings settings
    Project project
    //to keep track of used elastic dependencies
    HashMap<String,String> elasticLibs

    @Override
    void apply(Object o) {
        if (o in Settings) applyTo(o as Settings)
        if (o in Project) applyTo(o as Project)
    }

    void applyTo(Settings settings) {
        this.settings = settings;
        settings.metaClass.mixin(SettingsUtils)
    }

    void applyTo(Project project) {
        this.project = project;
        this.elasticLibs = new HashMap<>()
        project.metaClass.projectExists = this.&projectExists
        project.metaClass.elasticLibs = this.elasticLibs
        project.dependencies.metaClass.compileElasticLib = this.&compileElasticLib
    }

    boolean projectExists (String projectName) {
        project.findProject(projectName) != null
    }

    def compileElasticLib(String libraryProjectName, String libraryMavenName, Object o = null) {
        elasticLibs.put(libraryProjectName, libraryMavenName)
        if( projectExists(libraryProjectName) ) {
            def projectDep = project.project(libraryProjectName)
            projectDep.group = "local.build"
            project.dependencies.compile projectDep
            excludeTransitions project, libraryMavenName
        }
        else if (o != null) project.dependencies.compile libraryMavenName, o
        else project.dependencies.compile libraryMavenName
    }

    static def excludeTransitions(Project project, String notation) {
        String[] splitNotation = notation.split(":")
        if ( splitNotation.length == 3 ) {
            project.allprojects {
                project.configurations.all*.exclude group: splitNotation[0], module: splitNotation[1]
            }
        }
    }
}
