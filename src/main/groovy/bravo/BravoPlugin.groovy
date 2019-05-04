package bravo

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy


// CREATE CUSTOM dataFiles DEPENDENCY - SEE https://guides.gradle.org/implementing-gradle-plugins/

class BravoPlugin implements Plugin<Project> {

    String installLoc = ""

    void apply(Project project) {

        project.task('depenv') {
            doLast {
                println "Generating dep.env"
                def selectedDeps = project.configurations.external.incoming.resolutionResult.allDependencies.collect { dep ->
                    "${dep.selected}"
                }
                new File("$project.projectDir/dep.env").text = selectedDeps.unique().sort().join("\n")
            }
        }

        project.configurations {
            external
        }

        project.task("install", type: Copy, overwrite: true) {
            if (!project.hasProperty("install_loc")) {
                throw new GradleException('-Pinstall_loc required')
            }

            this.installLoc = project.getProperty('install_loc')
            println "Installing deps to " + this.installLoc

            from project.configurations.external
            into  this.installLoc
        }

        project.build.dependsOn  'depenv' // 'copyDependencies',
    }
}