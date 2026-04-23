package dev.nevack.unitconverter.history

import javax.inject.Inject

class ClearHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        suspend operator fun invoke() {
            historyRepository.deleteAll()
        }
    }
