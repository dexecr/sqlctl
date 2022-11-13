GRAAL_VM_IMAGE_VERSION=ghcr.io/graalvm/native-image:ol7-java17-22.2.0

docker run --rm --entrypoint "" \
  -w /opt/sqlctl \
  -v "${PWD}":/opt/sqlctl/ \
  -u "$(id -u "${USER}")":"$(id -g "${USER}")" \
  $GRAAL_VM_IMAGE_VERSION \
  bash ./_build_native_image.sh
