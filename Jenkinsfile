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
    } catch(Exception ex) {
        throw ex
    }
}