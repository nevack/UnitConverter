package dev.nevack.unitconverter.history.usecase

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import javax.inject.Inject

class RemoveHistoryItemUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        suspend operator fun invoke(item: HistoryRecord) {
            historyRepository.delete(item)
        }
    }
