pipeline {
    agent any
    parameters {
        booleanParam(name: "RELEASE",
                description: "Build a release from current commit.",
                defaultValue: false)
    }
    stages {
        stage ('Build') {
            steps {
                sh 'mvn install deploy'
                sh 'sudo /opt/bin/tomcatmavendeploy /var/lib/tomcat-8/vaska/webapps/filedossier-web.mavendeploy'
            }
        }
        stage("Release") {
            when {
                expression { params.RELEASE }
            }
            steps {
                sh "mvn -B release:prepare"
                sh "mvn -B release:perform"
            }
        }
    }
    post {
        always {
            deleteDir()
        }
    }
}
