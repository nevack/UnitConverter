package dev.nevack.unitconverter.history.usecase

import dev.nevack.unitconverter.history.HistoryRecord
import dev.nevack.unitconverter.history.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        operator fun invoke(): Flow<List<HistoryRecord>> = historyRepository.observeAll()
    }
