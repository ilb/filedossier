pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                sh 'mvn install'
                sh 'sudo /opt/bin/tomcatmavendeploy /var/lib/tomcat-8/vaska/webapps/filedossier-web.mavendeploy'
            }
        }
        stage("Release") {
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
