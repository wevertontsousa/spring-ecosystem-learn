FROM amazoncorretto:21-alpine3.19

ARG APPLICATION_HOME_PATH
ARG APPLICATION_PORT
ARG APPLICATION_ROOT_PATH
ARG APPLICATION_USER_ID
ARG APPLICATION_USERNAME

RUN apk update && apk upgrade \
  ; apk add --no-cache bash git openssh-client starship \
  ; rm -rf /var/cache/apk/* \
  ; USERNAME=$(getent passwd $APPLICATION_USER_ID | cut -d: -f1) || true \
  ; if [ -n "$USERNAME" ]; then deluser --remove-home $USERNAME || true; delgroup $USERNAME || true; fi \
  ; addgroup -g $APPLICATION_USER_ID $APPLICATION_USERNAME \
  ; adduser -D -G $APPLICATION_USERNAME -u $APPLICATION_USER_ID -s /bin/bash $APPLICATION_USERNAME \
  && echo 'eval "$(starship init bash)"' >> $APPLICATION_HOME_PATH/.bashrc

USER $APPLICATION_USERNAME

WORKDIR $APPLICATION_ROOT_PATH

EXPOSE $APPLICATION_PORT
