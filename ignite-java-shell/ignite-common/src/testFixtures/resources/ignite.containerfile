
FROM debian:12.7-slim

RUN apt-get update \
	&& apt-get install -y procps curl wget diffutils lsof netcat-traditional \
       net-tools telnet tree tcpdump openssh-client default-mysql-client \
       bat less unzip zip yq uuid-runtime  apache2-utils \
    && wget -O /tmp/jdk-17.0.10+7.tar.gz  https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.10%2B7/OpenJDK17U-jdk_x64_linux_hotspot_17.0.10_7.tar.gz \
    && mkdir -p /opt/openjdk17 \
    && tar -zxf /tmp/jdk-17.0.10+7.tar.gz -C /opt/openjdk17 --strip-components=1 \
    && rm -f /tmp/jdk-17.0.10+7.tar.gz
RUN cd /tmp \
    && wget --no-check-certificate https://archive.apache.org/dist/ignite/2.16.0/apache-ignite-slim-2.16.0-bin.zip \
    && unzip -q apache-ignite-slim-2.16.0-bin.zip \
    && mv apache-ignite-slim-2.16.0-bin ignite

ENTRYPOINT [ "/opt/jre17/bin/java", "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED", "--add-opens=java.management/com.sun.jmx.mbeanserver=ALL-UNNAMED", "--add-opens=jdk.internal.jvmstat/sun.jvmstat.monitor=ALL-UNNAMED", "--add-opens=java.base/sun.reflect.generics.reflectiveObjects=ALL-UNNAMED", "--add-opens=jdk.management/com.sun.management.internal=ALL-UNNAMED", "--add-opens=java.base/java.io=ALL-UNNAMED", "--add-opens=java.base/java.nio=ALL-UNNAMED", "--add-opens=java.base/java.util=ALL-UNNAMED", "--add-opens=java.base/java.lang=ALL-UNNAMED", "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED","--add-opens=java.base/java.net=ALL-UNNAMED", "-Xms512m", "-Xmx1g", "-server", "-XX:MaxMetaspaceSize=256m", "-DIGNITE_HOME=/tmp/ignite/apache-ignite", "-DIGNITE_QUIET=false" , "-cp", "/tmp/ignite/apache-ignite/libs/*:/tmp/ignite/apache-ignite/libs/*:/tmp/ignite/apache-ignite/libs/ignite-control-utility/*:/tmp/ignite/apache-ignite/libs/ignite-indexing/*:/tmp/ignite/apache-ignite/libs/ignite-spring/*:/tmp/ignite/apache-ignite/libs/licenses/*:/tmp/ignite/apache-ignite/libs/optional/ignite-kubernetes/*:/tmp/ignite/apache-ignite/libs/optional/ignite-rest-http/*:/tmp/ignite/apache-ignite/libs/optional/ignite-log4j2/*", "org.apache.ignite.startup.cmdline.CommandLineStartup", "/tmp/ignite/apache-ignite/config/default-config.xml"]


 



