# Spring REST Docs Kotlin DSL

A Kotlin DSL for writing Spring REST Docs and `restdocs-api-spec` documentation with less boilerplate.

## Quick start

Add the integration module that matches your documentation workflow to `build.gradle.kts`.

```kotlin
dependencies {
    testImplementation("io.github.junggikim:restdocs-kotlin-dsl-api-spec-mockmvc:0.1.2")
}
```

Use `restdocs-kotlin-dsl-mockmvc` when you only need Spring REST Docs snippets. Use `restdocs-kotlin-dsl-api-spec-mockmvc` when you also need OpenAPI 3 resources.

```kotlin
import io.github.junggikim.restdocsdsl.DocType
import io.github.junggikim.restdocsdsl.enumValues
import io.github.junggikim.restdocsdsl.example
import io.github.junggikim.restdocsdsl.field
import io.github.junggikim.restdocsdsl.means
import io.github.junggikim.restdocsdsl.parameter
import io.github.junggikim.restdocsdsl.apispec.documentApi

mockMvc.documentApi(get("/users/{userId}", 1)) {
    identifier = "users-get"
    tag = "Users"
    summary = "Get a user"
    description = "Returns one user by identifier."

    pathParameters = listOf(
        "userId" parameter DocType.Number means "User identifier" example 1,
    )
    responseFields = listOf(
        "id" field DocType.Number means "User identifier" example 1,
        "name" field DocType.String means "Display name" example "Ada",
        "role" field enumValues(Role::class) means "Granted role" example Role.MEMBER,
    )
}
```

`documentApi` generates REST Docs snippets and the `restdocs-api-spec` `resource.json` together. Configure the OpenAPI YAML generation task according to the official `restdocs-api-spec` documentation.

## Examples

### Plain Spring REST Docs

Use `documentRestDocs` when OpenAPI output is not required. It performs the request and writes only standard Spring REST Docs snippets.

```kotlin
import io.github.junggikim.restdocsdsl.DocType
import io.github.junggikim.restdocsdsl.example
import io.github.junggikim.restdocsdsl.field
import io.github.junggikim.restdocsdsl.means
import io.github.junggikim.restdocsdsl.mockmvc.documentRestDocs

mockMvc.documentRestDocs(get("/health")) {
    identifier = "health-get"
    responseFields = listOf(
        "status" field DocType.String means "Service status" example "UP",
    )
}
```

### JSON request body and optional fields

Use `optional()` only for values that clients may omit. All other fields are required by default.

```kotlin
import io.github.junggikim.restdocsdsl.DocType
import io.github.junggikim.restdocsdsl.example
import io.github.junggikim.restdocsdsl.field
import io.github.junggikim.restdocsdsl.means
import io.github.junggikim.restdocsdsl.apispec.documentApi
import org.springframework.http.MediaType

mockMvc.documentApi(
    post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content("""{"name":"Ada","bio":"Writes documentation"}"""),
) {
    identifier = "users-create"
    tag = "Users"
    summary = "Create a user"
    requestFields = listOf(
        "name" field DocType.String means "Display name" example "Ada",
        ("bio" field DocType.String means "Short biography").optional() example "Writes documentation",
    )
    responseFields = listOf(
        "id" field DocType.Number means "Created user identifier" example 1,
        "name" field DocType.String means "Display name" example "Ada",
    )
}
```

### Query parameters and request headers

Document query parameters and headers independently from JSON fields. The DSL preserves the type and sample as descriptor attributes.

```kotlin
import io.github.junggikim.restdocsdsl.DocType
import io.github.junggikim.restdocsdsl.example
import io.github.junggikim.restdocsdsl.header
import io.github.junggikim.restdocsdsl.means
import io.github.junggikim.restdocsdsl.parameter
import io.github.junggikim.restdocsdsl.apispec.documentApi

mockMvc.documentApi(
    get("/users")
        .queryParam("page", "0")
        .header("X-Request-Id", "request-123"),
) {
    identifier = "users-list"
    queryParameters = listOf(
        ("page" parameter DocType.Number means "Zero-based page index").optional() example 0,
        ("size" parameter DocType.Number means "Page size").optional() example 20,
    )
    requestHeaders = listOf(
        "X-Request-Id" header DocType.String means "Client request identifier" example "request-123",
    )
}
```

### Enum values

`enumValues` records the enum values in the descriptor and appends them to the field description.

```kotlin
enum class Role { ADMIN, MEMBER }

val responseFields = listOf(
    "role" field enumValues(Role::class) means "Granted role" example Role.MEMBER,
)
```

### Multipart request parts

Use `part` for every multipart part. `requestParts` produces the standard Spring REST Docs `request-parts` snippet.

```kotlin
import io.github.junggikim.restdocsdsl.DocType
import io.github.junggikim.restdocsdsl.example
import io.github.junggikim.restdocsdsl.means
import io.github.junggikim.restdocsdsl.part
import io.github.junggikim.restdocsdsl.apispec.documentApi

mockMvc.documentApi(multipart("/avatars").file("avatar", imageBytes)) {
    identifier = "avatars-upload"
    tag = "Avatars"
    requestParts = listOf(
        "avatar" part DocType.String means "Image file" example "avatar.png",
        ("caption" part DocType.String means "Image caption").optional() example "Profile photo",
    )
}
```

## DSL reference

| Target | Entry function | Optional value |
| --- | --- | --- |
| JSON field | `"name" field DocType.String` | `.optional()` |
| Path/query parameter | `"page" parameter DocType.Number` | `.optional()` |
| Header | `"X-Request-Id" header DocType.String` | `.optional()` |
| Multipart part | `"file" part DocType.String` | `.optional()` |

Supported types are `Array`, `Boolean`, `Number`, `Object`, `String`, `Date`, `DateTime`, and `Null`. Use `enumValues(MyEnum::class)` or `groupedValues(...)` for enum values.

## Modules

| Artifact | Purpose |
| --- | --- |
| `restdocs-kotlin-dsl` | Descriptor DSL and immutable documentation model |
| `restdocs-kotlin-dsl-mockmvc` | Plain Spring REST Docs `MockMvc` integration |
| `restdocs-kotlin-dsl-api-spec-mockmvc` | `restdocs-api-spec` resource integration |

## Compatibility

- Java 17 or later
- Compiled and tested with Kotlin 2.1
- Spring REST Docs 3.0.x
- `restdocs-api-spec` 0.19.4

## Multipart OpenAPI

A Gradle plugin for vendor-specific multipart `resource.json` and OpenAPI YAML enrichment is planned for 0.2.0. The current library generates standard Spring REST Docs `requestParts` snippets.

## Development

```bash
./gradlew test
./gradlew build
```

## Publishing

Publishing to Maven Central requires a Central Portal namespace, a user token, and a public GPG key. Configure these GitHub Actions secrets, then manually run the `Publish to Maven Central` workflow.

- `MAVEN_CENTRAL_USERNAME`
- `MAVEN_CENTRAL_PASSWORD`
- `SIGNING_IN_MEMORY_KEY`
- `SIGNING_IN_MEMORY_KEY_PASSWORD`

## License

Apache License 2.0. See [LICENSE](LICENSE).

## Security

Do not report vulnerabilities in a public issue. Follow the process in [SECURITY.md](SECURITY.md).
