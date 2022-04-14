node {
  stage('SCM') {
    checkout scm
  }
  stage('SonarQube Analysis') {
    withSonarQubeEnv() {
      bat "./gradlew clean build jacocoRootReport sonarqube"
    }
  }
}
