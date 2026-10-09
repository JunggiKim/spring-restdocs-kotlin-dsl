# Contributing

## Development setup

Java 17 이상에서 `./gradlew test`를 실행합니다. 변경은 해당 모듈의 Kotest 테스트를 먼저 추가한 뒤 구현합니다.

## Pull requests

- 공개 API 변경에는 README 예시와 호환성 영향을 포함합니다.
- 사내 시스템명, URL, 데이터, 인증 정보, proprietary package name을 포함하지 않습니다.
- formatter나 관련 없는 리팩터링을 섞지 않습니다.
- 모든 테스트와 `./gradlew build`가 통과해야 합니다.

## Versioning

Semantic Versioning을 사용합니다. public API를 깨는 변경은 major version으로 올립니다.
