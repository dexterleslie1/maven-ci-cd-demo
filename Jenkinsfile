node {
    try {
        node("slave-build") {
            stage "Checkout"
            sh "y | docker image prune"
            sh "rm -rf \$(pwd)/src"
            // sh "docker run --rm -v \$(pwd):/git alpine/git clone https://github.com/dexterleslie1/maven-ci-cd-demo.git src"
            sh "curl -s --output settings.xml https://bucket-public-common.oss-cn-hangzhou.aliyuncs.com/settings.xml"

            stage "Compile"
            // sh "docker run --rm -v \$(pwd)/src:/usr/src/mymaven -v \$(pwd)/src/settings.xml:/usr/share/maven/ref/settings.xml -v volume-maven-repo:/root/.m2 -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean package"
            docker.image("maven:3.3-jdk-8").inside("-v ${env.WORKSPACE}/settings.xml:/usr/share/maven/ref/settings.xml -v volume-maven-repo:/root/.m2") {
                sh "git clone https://github.com/dexterleslie1/maven-ci-cd-demo.git src"
                dir("src") {
                    sh "mvn clean package"
                }
            }

            stage "Build docker"
            dir("src") {
                docker.withRegistry("https://registry.cn-hangzhou.aliyuncs.com", "credentialDockerAli") {
                    def imageVariable = docker.build("registry.cn-hangzhou.aliyuncs.com/future-common/maven-ci-cd-demo")
                    imageVariable.push()
                }
            }
        }

        node("slave-build") {
            stage "Deploy"

            sh '''
                #!/bin/bash

                containerIds=$(docker ps -a --format "{{.ID}}" --filter "ancestor=registry.cn-hangzhou.aliyuncs.com/future-common/maven-ci-cd-demo")
                for containerId in $containerIds; do
                        docker rm -f $containerId
                done
            '''

            docker.withRegistry("https://registry.cn-hangzhou.aliyuncs.com") {
                def imageVariable = docker.image("registry.cn-hangzhou.aliyuncs.com/future-common/maven-ci-cd-demo")
                imageVariable.pull()
                imageVariable.run("-p 80:8080")
                
                sh '''
                    #!/bin/bash
                    while [ "`curl -s http://localhost/status`" != "Ready" ]
                    do
                            echo "Wait for maven-ci-cd-demo service ready..."
                            sleep 1
                    done
                '''
            }
        }
    } catch(Exception ex) {
        throw ex
    }
}