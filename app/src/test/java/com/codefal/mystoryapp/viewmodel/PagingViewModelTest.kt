package com.codefal.mystoryapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.codefal.mystoryapp.DataDummy
import com.codefal.mystoryapp.MainDispatcherRule
import com.codefal.mystoryapp.getOrAwaitValue
import com.codefal.mystoryapp.network.model.ListStoryItem
import com.codefal.mystoryapp.repository.PagingRepository
import com.codefal.mystoryapp.view.adapter.MyItemAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PagingViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: PagingRepository

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummy = DataDummy.generateDummyQuoteResponse()
        val data: PagingData<ListStoryItem> = StoryPagingSource.snapshot(dummy)
        val expected = MutableLiveData<PagingData<ListStoryItem>>()
        expected.value = data
        Mockito.`when`(repository.getStory("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUx1Z0kxR1B4ZEJrQm9LWnkiLCJpYXQiOjE2ODc0NDY3MjV9.ciQXlGK53npjTUrI6puoKALDtBH06VD_Zv4VTGH13v4")).thenReturn(expected)

        val viewModel = PagingViewModel(repository)
        val actualStory: PagingData<ListStoryItem> = viewModel.story("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUx1Z0kxR1B4ZEJrQm9LWnkiLCJpYXQiOjE2ODc0NDY3MjV9.ciQXlGK53npjTUrI6puoKALDtBH06VD_Zv4VTGH13v4").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MyItemAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummy.size, differ.snapshot().size)
        assertEquals(dummy[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoryItem> = PagingData.from(emptyList())
        val expected = MutableLiveData<PagingData<ListStoryItem>>()
        expected.value = data
        Mockito.`when`(repository.getStory("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUx1Z0kxR1B4ZEJrQm9LWnkiLCJpYXQiOjE2ODc0NDY3MjV9.ciQXlGK53npjTUrI6puoKALDtBH06VD_Zv4VTGH13v4")).thenReturn(expected)

        val viewModel = PagingViewModel(repository)
        val actual: PagingData<ListStoryItem> = viewModel.story("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUx1Z0kxR1B4ZEJrQm9LWnkiLCJpYXQiOjE2ODc0NDY3MjV9.ciQXlGK53npjTUrI6puoKALDtBH06VD_Zv4VTGH13v4").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = MyItemAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actual)

        Assert.assertEquals(0, differ.snapshot().size)
    }

    class StoryPagingSource: PagingSource<Int, LiveData<List<ListStoryItem>>>() {
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
        companion object {
            fun snapshot(items: List<ListStoryItem>): PagingData<ListStoryItem> {
                return PagingData.from(items)
            }
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}