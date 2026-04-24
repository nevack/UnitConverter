package dev.nevack.unitconverter.history

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
