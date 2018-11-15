import jetbrains.buildServer.configs.kotlin.v2018_1.*
import jetbrains.buildServer.configs.kotlin.v2018_1.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_1.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_1.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2018.1"

project {

    vcsRoot(GitGithubComShengyouKotlindslLaravelDemoGitRefsHeadsMaster)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    artifactRules = """
        coverage => coverage.zip
        clover.xml => clover.xml
    """.trimIndent()

    vcs {
        root(GitGithubComShengyouKotlindslLaravelDemoGitRefsHeadsMaster)
    }

    steps {
        script {
            name = "Install Dependancy"
            scriptContent = """
                composer install --no-scripts
                composer run-script post-root-package-install
                composer run-script post-create-project-cmd
                composer run-script post-autoload-dump
            """.trimIndent()
        }
        script {
            name = "Test"
            scriptContent = """
                ./vendor/bin/phpunit --teamcity --coverage-html=coverage --coverage-clover=clover.xml
                ./vendor/bin/teamcity-clover clover.xml
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }
})

object GitGithubComShengyouKotlindslLaravelDemoGitRefsHeadsMaster : GitVcsRoot({
    name = "git@github.com:shengyou/kotlindsl-laravel-demo.git#refs/heads/master"
    pollInterval = 10
    url = "git@github.com:shengyou/kotlindsl-laravel-demo.git"
    branchSpec = "+:refs/heads/*"
    authMethod = uploadedKey {
        userName = "git"
        uploadedKey = "Kaith"
    }
})
