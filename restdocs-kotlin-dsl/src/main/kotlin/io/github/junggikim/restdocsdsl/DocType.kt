package io.github.junggikim.restdocsdsl

import kotlin.reflect.KClass
import org.springframework.restdocs.payload.JsonFieldType

sealed class DocType protected constructor(internal val jsonType: JsonFieldType) {
    data object Array : DocType(JsonFieldType.ARRAY)
    data object Boolean : DocType(JsonFieldType.BOOLEAN)
    data object Number : DocType(JsonFieldType.NUMBER)
    data object Object : DocType(JsonFieldType.OBJECT)
    data object String : DocType(JsonFieldType.STRING)
    data object Date : DocType(JsonFieldType.STRING)
    data object DateTime : DocType(JsonFieldType.STRING)
    data object Null : DocType(JsonFieldType.NULL)
    class Enum internal constructor(val values: List<kotlin.String>) : DocType(JsonFieldType.STRING)
}

fun <T : Enum<T>> enumValues(type: KClass<T>): DocType.Enum = DocType.Enum(type.java.enumConstants.map(Enum<*>::name))

fun enumValues(vararg values: Enum<*>): DocType.Enum = DocType.Enum(values.map(Enum<*>::name))

fun groupedValues(vararg groups: Pair<kotlin.String, Iterable<kotlin.String>>): DocType.Enum = DocType.Enum(
    groups.asSequence().flatMap { (_, values) -> values.asSequence() }.map(kotlin.String::trim).filter(kotlin.String::isNotBlank).distinct().toList(),
)
