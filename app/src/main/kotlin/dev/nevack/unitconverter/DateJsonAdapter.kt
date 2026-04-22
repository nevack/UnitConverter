package dev.nevack.unitconverter

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateJsonAdapter : KSerializer<Date> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("dev.nevack.unitconverter.DateJsonAdapter", PrimitiveKind.STRING)

    private val formatter =
        ThreadLocal.withInitial {
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        }

    override fun deserialize(decoder: Decoder): Date = formatter.get()!!.parse(decoder.decodeString())!!

    override fun serialize(
        encoder: Encoder,
        value: Date,
    ) {
        encoder.encodeString(formatter.get()!!.format(value))
    }
}
