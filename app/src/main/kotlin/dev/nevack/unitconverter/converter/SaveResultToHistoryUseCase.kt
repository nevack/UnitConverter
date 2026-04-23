package dev.nevack.unitconverter.converter

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import dev.nevack.unitconverter.model.converter.Converter
import javax.inject.Inject

class SaveResultToHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        suspend operator fun invoke(
            converter: Converter,
            categoryId: String,
            result: ConvertData,
        ) {
            historyRepository.save(
                HistoryRecord(
                    unitFrom = converter[result.from].name,
                    unitTo = converter[result.to].name,
                    valueFrom = result.value,
                    valueTo = result.result,
                    categoryId = categoryId,
                ),
            )
        }
    }
