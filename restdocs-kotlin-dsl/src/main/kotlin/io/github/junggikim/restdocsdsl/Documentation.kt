package io.github.junggikim.restdocsdsl

/** A complete API-documentation declaration, independent of a web framework. */
data class Documentation(
    val identifier: kotlin.String,
    val description: kotlin.String? = null,
    val summary: kotlin.String? = null,
    val tag: kotlin.String? = null,
    val deprecated: Boolean = false,
    val privateResource: Boolean = false,
    val requestFields: List<Field> = emptyList(),
    val responseFields: List<Field> = emptyList(),
    val requestHeaders: List<Header> = emptyList(),
    val responseHeaders: List<Header> = emptyList(),
    val pathParameters: List<Parameter> = emptyList(),
    val queryParameters: List<Parameter> = emptyList(),
    val requestParts: List<RequestPart> = emptyList(),
)

/** Mutable only at the Kotlin-DSL boundary; [build] always returns an immutable value. */
class DocumentationBuilder {
    var identifier: kotlin.String = ""
    var description: kotlin.String? = null
    var summary: kotlin.String? = null
    var tag: kotlin.String? = null
    var deprecated: Boolean = false
    var privateResource: Boolean = false
    var requestFields: List<Field> = emptyList()
    var responseFields: List<Field> = emptyList()
    var requestHeaders: List<Header> = emptyList()
    var responseHeaders: List<Header> = emptyList()
    var pathParameters: List<Parameter> = emptyList()
    var queryParameters: List<Parameter> = emptyList()
    var requestParts: List<RequestPart> = emptyList()

    fun build(): Documentation {
        require(identifier.isNotBlank()) { "identifier must not be blank" }
        return Documentation(
            identifier = identifier,
            description = description,
            summary = summary,
            tag = tag,
            deprecated = deprecated,
            privateResource = privateResource,
            requestFields = requestFields,
            responseFields = responseFields,
            requestHeaders = requestHeaders,
            responseHeaders = responseHeaders,
            pathParameters = pathParameters,
            queryParameters = queryParameters,
            requestParts = requestParts,
        )
    }
}

fun documentation(block: DocumentationBuilder.() -> Unit): Documentation = DocumentationBuilder().apply(block).build()
