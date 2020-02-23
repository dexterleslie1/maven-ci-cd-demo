node {
    try {
        node("slave-centos7") {
            stage "Checkout"
            sh "rm -rf \$(pwd)/src"
            sh "docker run --rm -v \$(pwd):/git alpine/git clone https://github.com/dexterleslie1/maven-ci-cd-demo.git src"

            stage "Compile"
            sh "docker run --rm -v \$(pwd)/src:/usr/src/mymaven -v \$(pwd)/src/settings.xml:/usr/share/maven/ref/settings.xml -v volume-maven-repo:/root/.m2 -w /usr/src/mymaven maven:3.3-jdk-8 mvn clean package"

            stage "Build docker"
            dir("src") {
                docker.withRegistry("https://registry.cn-hangzhou.aliyuncs.com", "credentialDockerAli") {
                    def imageVariable = docker.build("registry.cn-hangzhou.aliyuncs.com/future-common/maven-ci-cd-demo")
                    imageVariable.push()
                }
            }
        }

        node("slave-centos7-deploy") {
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
                sh "while ! nc -zv localhost 80; do sleep 1; done"
            }
        }
    } catch(Exception ex) {
        throw ex
    }
}