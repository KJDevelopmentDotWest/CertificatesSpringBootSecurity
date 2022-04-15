node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    withSonarQubeEnv() {
      bat "./gradlew clean build service:jacocoTestReport jacocoRootReport sonarqube"
    }
  }
}
