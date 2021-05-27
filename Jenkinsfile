pipeline {
    agent any
    environment {
        COMMIT_HASH = "${sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()}"
    }
    stages {
        stage('Clean and test target') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Test and Package') {
            steps {
                sh 'mvn package'
            }
        }
 //       stage('Code Analysis: SonarQube') {
 //           steps {
 //               withSonarQubeEnv('SonarQube') {
 //                   sh 'mvn sonar:sonar -Dsonar.login=fe2fd4de999e222d92ab830601a6d0e663cc1cbe'
 //               }
 //           }
 //       }
 //        stage('Quality gate') {
 //          steps {
 //              waitForQualityGate abortPipeline: true
 //            }
 //        }
            stage('Docker Build') {
            steps {
                echo 'Deploying....'
                sh "docker build --tag flight-service:$COMMIT_HASH ."
                 sh "docker tag flight-service:$COMMIT_HASH 135316859264.dkr.ecr.us-east-2.amazonaws.com/flight-service:$COMMIT_HASH"
                 sh "docker push 135316859264.dkr.ecr.us-east-2.amazonaws.com/flight-service:$COMMIT_HASH"
            }
        }
          stage('Deploy') {
                steps {
                  echo 'Fetching cloud cloudformation template..'
                  sh "touch ECS.yml"
                  sh "rm ECS.yml"
                  sh "wget https://raw.githubusercontent.com/Java-Feb-CRAM/cloud-formation/main/ECS.yml"
                  echo 'Deploying cloudformation..'
                  sh "aws cloudformation deploy --stack-name UtopiaFlightPlaneMS --template-file ./ECS.yml --parameter-overrides ApplicationName=FlightPlaneMS ECRepositoryUri=038778514259.dkr.ecr.us-east-1.amazonaws.com/utopia-flight-plane:$COMMIT_HASH ExecutionRoleArn=arn:aws:iam::038778514259:role/ecsTaskExecutionRole TargetGroupArn=arn:aws:elasticloadbalancing:us-east-1:038778514259:targetgroup/FlightPlaneTG/89c35ebca3b8e8fe --role-arn arn:aws:iam::038778514259:role/CloudFormationECS --capabilities CAPABILITY_IAM CAPABILITY_NAMED_IAM --region us-east-1"
                }
                post {
                  always {
                    jiraSendDeploymentInfo site: 'java-feb-cram.atlassian.net', environmentId: 'us-prod-1', environmentName: 'us-prod-1', environmentType: 'production'
                  }
                }
              }
    }
    post {
        always {
            sh 'mvn clean'
            sh 'docker image prune'
        }
    }
}