# gradle-elastic-libs [![Build Status](https://travis-ci.org/amaksoft/gradle-elastic-libs-plugin.svg?branch=master)](https://travis-ci.org/amaksoft/gradle-elastic-libs-plugin) [ ![Download](https://api.bintray.com/packages/amaksoft/maven/gradle-elastic-libs-plugin/images/download.svg) ](https://bintray.com/amaksoft/maven/gradle-elastic-libs-plugin/_latestVersion)

Gradle plugin to easily switch between local project and repository artifacts of your libs for debugging.

## Inspired by
* [elastic-deps](https://github.com/pniederw/elastic-deps): elastic-deps by [Peter Niederwieser](https://github.com/pniederw/)
* [gradle-elasticdependencies](https://github.com/stackmagic/gradle-elasticdependencies): gradle-elasticdependencies by [Patrick Huber](https://github.com/stackmagic)

## Usage

Right now you can switch between local project and artifact by commenting/uncommenting `includeIfValid` in settings.gradle

The idea is to validate if project exists before including it into a multi-project build (in case someone pushed his commit with uncommented lines in settings.gradle to youg git repo) and add it to dependencies. Or using a repository artifact in any other case.

The plugin adds `includeIfValid(String projectName, String projectPath)` method to settings.gradle and `compileElasticLib(String localProjectName, String mavenArtifactNotation, Object o)` to build.gradle dependencies extension.

Here is an example:
```groovy

settings.gradle:

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.github.amaksoft:gradle-elastic-libs-plugin:0.0.5'
    }
}

apply plugin: 'com.github.amaksoft.elasticlibs'

// ...

include ":ProjectOne" 
include ":ProjectTwo"

// ...

// Don't include local projects if environment variable exists ("JENKINS_URL" by default)
envVarSwitch = "NO_LOCAL_PROJECTS"

//...

// include only if it is a valid project (directory exists and there is a build.gradle file in it)
includeIfValid ":YourLibOne", "path/to/your/lib/one"
includeIfValid ":YourLibTwo", "path/to/your/lib/two"
//includeIfValid ":YourLibThree", "path/to/your/lib/three"


build.gradle (ProjectOne):

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.github.amaksoft:gradle-elastic-libs-plugin:0.0.5'
    }
}

// ...

apply plugin: 'com.github.amaksoft.elasticlibs'

// ...

def versionLibOne = "1.1.1"
def versionLibTwo = "2.2.2"
def versionLibThree = "3.3.3"

dependencies {
    compileElasticLib ":YourLibOne", "com.example:lib_one:${versionLibOne}"

    compileElasticLib(":YourLibTwo", "com.example:lib_two:${versionLibTwo}") {
        artifact {
            name = 'lib_two-flavor'
            type = 'jar'
        }
    }
    
    compileElasticLib ":YourLibThree", "com.example:lib_three:${versionLibThree}" {
        artifact {
            name = 'lib_three-blah'
            type = 'jar'
        }
    }
}
// Keep track of your elastic libs using HashMap elasticLibs
HashMap<String, String> el = elasticLibs
el.each {println "Used elastic lib ${it.key} = ${it.value}"}
// Output:
//     Used elastic lib :YourLibOne = com.example:lib_one:1.1.1
//     Used elastic lib :YourLibTwo = com.example:lib_two:2.2.2

```

## Acknowledgements

Also i would like to thank [Soichiro Kashima](https://github.com/ksoichiro) for making [gradle-plugin-template](https://github.com/ksoichiro/gradle-plugin-template) which saved me a lot of time

## License

    Copyright 2016 Andrey Makeev

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
