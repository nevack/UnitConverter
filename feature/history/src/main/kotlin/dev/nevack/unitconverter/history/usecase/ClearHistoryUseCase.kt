package dev.nevack.unitconverter.history.usecase

import dev.nevack.unitconverter.history.HistoryRepository
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
