package dev.nevack.unitconverter.converter

import dev.nevack.unitconverter.model.converter.Converter
import javax.inject.Inject

class ConvertValueUseCase
    @Inject
    constructor() {
        operator fun invoke(
            converter: Converter?,
            data: ConvertData,
        ): String? =
            converter?.let {
                runCatching {
                    it.convert(
                        data.value,
                        data.from,
                        data.to,
                    )
                }.getOrNull()
            }
    }
