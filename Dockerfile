FROM gradle:5.1.1-jdk-alpine as builder
COPY --chown=gradle:gradle . /home/application
WORKDIR /home/application
RUN gradle build --no-daemon

FROM amazonlinux:2017.03.1.20170812 as graalvm
# install graal to amazon linux.
ENV LANG=en_US.UTF-8


RUN yum install -y gcc gcc-c++ libc6-dev  zlib1g-dev curl bash zlib zlib-devel zip  \
#    && yum install -y libcxx libcxx-devel llvm-toolset-7 \
    && rm -rf /var/cache/yum


ENV GRAAL_VERSION 1.0.0-rc13
ENV GRAAL_FILENAME graalvm-ce-${GRAAL_VERSION}-linux-amd64.tar.gz

RUN curl -4 -L https://github.com/oracle/graal/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENAME} -o /tmp/${GRAAL_FILENAME}

RUN tar -zxvf /tmp/${GRAAL_FILENAME} -C /tmp \
    && mv /tmp/graalvm-ce-${GRAAL_VERSION} /usr/lib/graalvm

RUN rm -rf /tmp/*
CMD ["/usr/lib/graalvm/bin/native-image"]

FROM graalvm
COPY --from=builder /home/application/ /home/application/
WORKDIR /home/application
RUN /home/application/build-native-image.sh
RUN chmod 755 bootstrap
RUN chmod 755 server
RUN zip -j function.zip bootstrap libsunec.so cacerts server
EXPOSE 8080
ENTRYPOINT ["/home/application/server"]
