node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    withSonarQubeEnv() {
      bat "./gradlew clean build jacocoTestReport jacocoRootReport sonarqube"
    }
  }
}
