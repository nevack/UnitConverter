package dev.nevack.unitconverter.history

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
    ) {
        operator fun invoke(): Flow<List<HistoryRecord>> = historyRepository.getAll()
    }
