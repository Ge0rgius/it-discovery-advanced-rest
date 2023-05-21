FROM ghcr.io/graalvm/graalvm-ce:22.3.2 as graalvm

RUN gu install native-image

ARG MAVEN_VERSION=3.9.2

ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries

RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
  && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
  && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
  && rm -f /tmp/apache-maven.tar.gz \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

COPY pom.xml /opt/pom.xml
COPY rest-server/pom.xml /opt/rest-server/pom.xml
WORKDIR /opt/rest-server
RUN mvn dependency:resolve
COPY . /opt/
RUN mvn -DskipTests -Pnative package

FROM ubuntu

COPY --from=graalvm /opt/rest-server/target/rest-server /graalvm

CMD ./graalvm
