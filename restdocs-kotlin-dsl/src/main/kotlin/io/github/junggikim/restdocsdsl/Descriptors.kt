package io.github.junggikim.restdocsdsl

import org.springframework.restdocs.headers.HeaderDescriptor
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.ParameterDescriptor
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestPartDescriptor
import org.springframework.restdocs.snippet.Attributes

private const val SAMPLE = "sample"
private const val ENUM_VALUES = "enumValues"
private const val FORMAT = "format"

data class Field(val path: kotlin.String, val type: DocType, val description: kotlin.String = "", val optional: Boolean = false, val sample: kotlin.String? = null) {
    fun optional(): Field = copy(optional = true)
    fun sample(value: Any?): Field = copy(sample = value?.toString())
    fun describedAs(value: kotlin.String): Field = copy(description = type.appendTo(value))

    fun toDescriptor(): FieldDescriptor {
        val descriptor = fieldWithPath(path).type(type.descriptorType())
        if (description.isNotBlank()) descriptor.description(description)
        if (optional) descriptor.optional()
        descriptor.attributes(*(type.attributes() + sampleAttribute(sample)).toTypedArray())
        return descriptor
    }
}

data class Parameter(val name: kotlin.String, val type: DocType = DocType.String, val description: kotlin.String = "", val optional: Boolean = false, val sample: kotlin.String? = null) {
    fun optional(): Parameter = copy(optional = true)
    fun sample(value: Any?): Parameter = copy(sample = value?.toString())
    fun describedAs(value: kotlin.String): Parameter = copy(description = type.appendTo(value))

    fun toDescriptor(): ParameterDescriptor {
        val descriptor = parameterWithName(name).description(description)
        if (optional) descriptor.optional()
        descriptor.attributes(*(listOf(Attributes.key("type").value(type.parameterType())) + type.attributes() + sampleAttribute(sample)).toTypedArray())
        return descriptor
    }
}

data class Header(val name: kotlin.String, val type: DocType = DocType.String, val description: kotlin.String = "", val optional: Boolean = false, val sample: kotlin.String? = null) {
    fun optional(): Header = copy(optional = true)
    fun sample(value: Any?): Header = copy(sample = value?.toString())
    fun describedAs(value: kotlin.String): Header = copy(description = type.appendTo(value))

    fun toDescriptor(): HeaderDescriptor {
        val descriptor = headerWithName(name).description(description)
        if (optional) descriptor.optional()
        descriptor.attributes(*(listOf(Attributes.key("type").value(type.parameterType())) + type.attributes() + sampleAttribute(sample)).toTypedArray())
        return descriptor
    }
}

data class RequestPart(val name: kotlin.String, val type: DocType = DocType.String, val description: kotlin.String = "", val optional: Boolean = false, val sample: kotlin.String? = null) {
    fun optional(): RequestPart = copy(optional = true)
    fun sample(value: Any?): RequestPart = copy(sample = value?.toString())
    fun describedAs(value: kotlin.String): RequestPart = copy(description = type.appendTo(value))

    fun toDescriptor(): RequestPartDescriptor {
        val descriptor = partWithName(name).description(description)
        if (optional) descriptor.optional()
        descriptor.attributes(*(listOf(Attributes.key("type").value(type.parameterType())) + type.attributes() + sampleAttribute(sample)).toTypedArray())
        return descriptor
    }
}

infix fun kotlin.String.field(type: DocType): Field = Field(path = this, type = type)
infix fun kotlin.String.parameter(type: DocType): Parameter = Parameter(name = this, type = type)
infix fun kotlin.String.header(type: DocType): Header = Header(name = this, type = type)
infix fun kotlin.String.part(type: DocType): RequestPart = RequestPart(name = this, type = type)
infix fun Field.means(description: kotlin.String): Field = describedAs(description)
infix fun Parameter.means(description: kotlin.String): Parameter = describedAs(description)
infix fun Header.means(description: kotlin.String): Header = describedAs(description)
infix fun RequestPart.means(description: kotlin.String): RequestPart = describedAs(description)
infix fun Field.example(value: Any?): Field = sample(value)
infix fun Parameter.example(value: Any?): Parameter = sample(value)
infix fun Header.example(value: Any?): Header = sample(value)
infix fun RequestPart.example(value: Any?): RequestPart = sample(value)

private fun DocType.descriptorType(): Any = if (this is DocType.Enum) "enum" else jsonType
private fun DocType.parameterType(): kotlin.String = if (this is DocType.Enum) "enum" else jsonType.name.lowercase()
private fun DocType.appendTo(description: kotlin.String): kotlin.String = when (this) {
    is DocType.Enum -> "$description [${values.joinToString(", ")}]"
    else -> description
}
private fun DocType.attributes(): List<Attributes.Attribute> = buildList {
    when (this@attributes) {
        DocType.Date -> add(Attributes.key(FORMAT).value("yyyy-MM-dd"))
        DocType.DateTime -> add(Attributes.key(FORMAT).value("yyyy-MM-dd'T'HH:mm:ssXXX"))
        is DocType.Enum -> add(Attributes.key(ENUM_VALUES).value(values))
        else -> Unit
    }
}
private fun sampleAttribute(sample: String?): List<Attributes.Attribute> = sample?.let { listOf(Attributes.key(SAMPLE).value(it)) }.orEmpty()
