package io.github.junggikim.restdocsdsl.mockmvc

import io.github.junggikim.restdocsdsl.Documentation
import io.github.junggikim.restdocsdsl.DocumentationBuilder
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.restdocs.snippet.Snippet
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.ResultActions

/** Performs a request and writes standard Spring REST Docs snippets. */
fun MockMvc.documentRestDocs(request: RequestBuilder, block: DocumentationBuilder.() -> Unit): ResultActions {
    val documentation = DocumentationBuilder().apply(block).build()
    return perform(request).andDo(MockMvcRestDocumentation.document(documentation.identifier, *documentation.snippets().toTypedArray()))
}

fun Documentation.snippets(): List<Snippet> = buildList {
    requestFields.takeIf(List<*>::isNotEmpty)?.let { add(requestFields(*it.map { field -> field.toDescriptor() }.toTypedArray())) }
    responseFields.takeIf(List<*>::isNotEmpty)?.let { add(responseFields(*it.map { field -> field.toDescriptor() }.toTypedArray())) }
    requestHeaders.takeIf(List<*>::isNotEmpty)?.let { add(requestHeaders(*it.map { header -> header.toDescriptor() }.toTypedArray())) }
    responseHeaders.takeIf(List<*>::isNotEmpty)?.let { add(responseHeaders(*it.map { header -> header.toDescriptor() }.toTypedArray())) }
    pathParameters.takeIf(List<*>::isNotEmpty)?.let { add(pathParameters(*it.map { parameter -> parameter.toDescriptor() }.toTypedArray())) }
    queryParameters.takeIf(List<*>::isNotEmpty)?.let { add(queryParameters(*it.map { parameter -> parameter.toDescriptor() }.toTypedArray())) }
    requestParts.takeIf(List<*>::isNotEmpty)?.let { add(requestParts(*it.map { part -> part.toDescriptor() }.toTypedArray())) }
}
