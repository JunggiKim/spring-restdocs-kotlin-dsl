package io.github.junggikim.restdocsdsl

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class DescriptorsTest : FunSpec({
    test("enum field includes values in its description and attributes") {
        val descriptor = ("role" field enumValues(Role::class) means "User role" example Role.ADMIN).toDescriptor()

        descriptor.type shouldBe "enum"
        descriptor.description shouldBe "User role [ADMIN, MEMBER]"
        descriptor.attributes["enumValues"] shouldBe listOf("ADMIN", "MEMBER")
        descriptor.attributes["sample"] shouldBe "ADMIN"
    }

    test("optional parameter remains optional and retains its sample") {
        val descriptor = (("page" parameter DocType.Number means "Page number") example 0).optional().toDescriptor()

        descriptor.isOptional shouldBe true
        descriptor.attributes["type"] shouldBe "number"
        descriptor.attributes["sample"] shouldBe "0"
    }

    test("documentation rejects a blank identifier") {
        val error = runCatching { documentation { identifier = " " } }.exceptionOrNull()

        error shouldNotBe null
        error?.message shouldBe "identifier must not be blank"
    }
}) {
    private enum class Role { ADMIN, MEMBER }
}
