package com.github.json

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias NullableString = String?

@OptIn(ExperimentalSerializationApi::class)
object NullableStringSerializer : KSerializer<NullableString> {
    override val descriptor = PrimitiveSerialDescriptor("java.lang.String", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): NullableString {
        if (decoder.decodeNotNullMark()) {
            return decoder.decodeString()
        }
        return decoder.decodeNull()
    }

    override fun serialize(encoder: Encoder, value: NullableString) {
        if (value == null) {
            encoder.encodeNull()
            return
        }
        encoder.encodeString(value)
    }
}