package de.abauer.giphy_androidcleanarchitecture.presentation.detail

import de.abauer.giphy_androidcleanarchitecture.base.BaseTest
import de.abauer.giphy_clean_architecture.presentation.detail.DetailGiphyState
import de.abauer.giphy_clean_architecture.presentation.detail.DetailGiphyViewModel
import io.uniflow.android.test.TestViewObserver
import io.uniflow.android.test.createTestObserver
import io.uniflow.core.flow.data.UIState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailGiphyViewModelTest : BaseTest(){
    lateinit var giphyViewModel : DetailGiphyViewModel
    lateinit var testObserver: TestViewObserver

    @Before
    fun before() {
        giphyViewModel = DetailGiphyViewModel()
        testObserver = giphyViewModel.createTestObserver()
    }

    @Test
    fun `receiving giphy updates state`() {
        val giphyUrl = "giphyUrl.com"
        giphyViewModel.onGiphyReceived(giphyUrl)

        testObserver.verifySequence (
            UIState.Empty,
            DetailGiphyState.LoadGiphy(giphyUrl)
        )
    }

    @Test
    fun `on share giphy updates state`() {
        val giphyUrl = "giphyUrl.com"

        giphyViewModel.onShareButtonClick(giphyUrl)

        testObserver.verifySequence (
            UIState.Empty,
            DetailGiphyState.ShareGiphy(giphyUrl)
        )
    }
}