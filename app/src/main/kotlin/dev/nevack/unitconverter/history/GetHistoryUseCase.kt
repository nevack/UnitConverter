package dev.nevack.unitconverter.history

import javax.inject.Inject

class GetHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        suspend operator fun invoke(): List<HistoryRecord> = historyRepository.getAll()
    }
