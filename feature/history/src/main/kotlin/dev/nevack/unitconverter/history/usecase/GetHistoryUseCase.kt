package dev.nevack.unitconverter.history.usecase

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import javax.inject.Inject

class GetHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        suspend operator fun invoke(): List<HistoryRecord> = historyRepository.getAll()
    }
