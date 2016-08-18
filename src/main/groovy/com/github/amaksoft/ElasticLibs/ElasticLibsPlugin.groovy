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
        project.rootProject.allprojects.toListString().contains(projectName)
    }

    def compileElasticLib(String libraryProjectName, String libraryMavenName, Object o = null) {
        elasticLibs.put(libraryProjectName, libraryMavenName)
        if( projectExists(libraryProjectName) )
            project.dependencies.compile project.project(libraryProjectName)
        else if (o != null) project.dependencies.compile libraryMavenName, o
        else project.dependencies.compile libraryMavenName
    }
}
