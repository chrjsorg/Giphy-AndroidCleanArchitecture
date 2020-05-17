package de.giphy_clean_architecture.domain.usecase

import de.giphy_clean_architecture.data.helper.ControlledRunner
import de.giphy_clean_architecture.domain.model.DataResult
import de.giphy_clean_architecture.domain.model.Giphy
import de.giphy_clean_architecture.domain.repository.TrendingGiphyRepository

class TrendingGiphysUseCase(
    private val trendingGiphyRepository: TrendingGiphyRepository,
    private val controlledRunner: ControlledRunner<DataResult<List<Giphy>>>
) {
    suspend fun getTrendingGiphys(): DataResult<List<Giphy>> =
        controlledRunner.joinPreviousOrRun {
            trendingGiphyRepository.getTrending()
        }
}