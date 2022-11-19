GRAAL_VM_IMAGE_VERSION=ghcr.io/graalvm/native-image:ol7-java17-22.2.0

SOURCE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

docker run --rm --entrypoint "" \
  -e GRADLE_USER_HOME="/opt/sqlctl/.gradle" \
  -w /opt/sqlctl \
  -v "${SOURCE_DIR}":/opt/sqlctl/ \
  -u "$(id -u "${USER}")":"$(id -g "${USER}")" \
  $GRAAL_VM_IMAGE_VERSION \
  bash ./_build_native_image.sh
