import org.gradle.api.Plugin
import org.gradle.api.Project

// CREATE CUSTOM dataFiles DEPENDENCY - SEE https://guides.gradle.org/implementing-gradle-plugins/

class BravoPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task('depenv') {
            doLast {
                println "Compile dependencies"
                def selectedDeps = project.configurations.compile.incoming.resolutionResult.allDependencies.collect { dep ->
                    "${dep.selected}"
                }
                new File("$buildDir/dep.env").text = selectedDeps.unique().sort().join("\n")

            }
        }
    }
}