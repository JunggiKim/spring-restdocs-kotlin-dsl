package io.github.junggikim.restdocsdsl.apispec

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document as apiSpecDocument
import io.github.junggikim.restdocsdsl.DocumentationBuilder
import io.github.junggikim.restdocsdsl.mockmvc.snippets
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions

/** Performs a request and writes Spring REST Docs snippets plus an API-spec resource. */
fun MockMvc.documentApi(request: RequestBuilder, block: DocumentationBuilder.() -> Unit): ResultActions {
    val documentation = DocumentationBuilder().apply(block).build()
    val resource = ResourceSnippetParametersBuilder()
        .description(documentation.description)
        .summary(documentation.summary)
        .tag(documentation.tag.orEmpty())
        .deprecated(documentation.deprecated)
        .privateResource(documentation.privateResource)
        .apply {
            documentation.requestFields.takeIf(List<*>::isNotEmpty)?.let { requestFields(*it.map { field -> field.toDescriptor() }.toTypedArray()) }
            documentation.responseFields.takeIf(List<*>::isNotEmpty)?.let { responseFields(*it.map { field -> field.toDescriptor() }.toTypedArray()) }
            documentation.pathParameters.takeIf(List<*>::isNotEmpty)?.let { pathParameters(*it.map { parameter -> parameter.toDescriptor() }.toTypedArray()) }
            documentation.queryParameters.takeIf(List<*>::isNotEmpty)?.let { queryParameters(*it.map { parameter -> parameter.toDescriptor() }.toTypedArray()) }
            documentation.requestHeaders.takeIf(List<*>::isNotEmpty)?.let { requestHeaders(*it.map { header -> header.toDescriptor() }.toTypedArray()) }
            documentation.responseHeaders.takeIf(List<*>::isNotEmpty)?.let { responseHeaders(*it.map { header -> header.toDescriptor() }.toTypedArray()) }
        }

    return perform(request).andDo(
        apiSpecDocument(
            identifier = documentation.identifier,
            resourceDetails = resource,
            snippets = documentation.snippets().toTypedArray(),
        ),
    )
}
