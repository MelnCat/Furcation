package dev.melncat.furcation.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

public object UUIDStringSerializer : KSerializer<UUID> {
	override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("java.util.UUID", PrimitiveKind.STRING)
	override fun serialize(encoder: Encoder, value: UUID): Unit = encoder.encodeString(value.toString())
	override fun deserialize(decoder: Decoder): UUID = UUID.fromString(decoder.decodeString())
}