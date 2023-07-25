package com.example.mywatchlistapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mywatchlistapp.data.Show
import com.example.mywatchlistapp.data.ShowDao
import kotlinx.coroutines.launch

class SharedViewModel(private val showDao: ShowDao) : ViewModel() {

    val allShows: LiveData<List<Show>> = showDao.getAllShows().asLiveData()

    /** Private Methods **/

    private fun insertShow(show: Show) {
        viewModelScope.launch { showDao.insert(show) }
    }

    private fun updateShow(show: Show) {
        viewModelScope.launch { showDao.update(show) }
    }

    private fun deleteShow(show: Show) {
        viewModelScope.launch { showDao.delete(show) }
    }

    /** Public Methods **/

    fun addNewShow(
        title: String,
        rating: Float,
        recommend: String,
        comment: String,
        timestamp: String
    ) {
        insertShow(
            Show(
                title = title,
                rating = rating,
                recommend = recommend,
                comment = comment,
                timestamp = timestamp
            )
        )
    }

    fun isInputValid(
        title: String,
        rating: Float,
        recommend: String
    ): Boolean {
        return !(title.isEmpty() || recommend.isEmpty() || rating == 0.0f)
    }

    fun getSpecificShow(id: Int): LiveData<Show> {
        // When you retrieve objects wrapped in Flow, return it as LiveData (to observe for the results)
        return showDao.getSpecificShow(id).asLiveData()
    }

    fun changeShowDetails(
        id: Int,
        title: String,
        rating: Float,
        recommend: String,
        comment: String,
        timestamp: String
    ) {
        val updatedShow = Show(id, title, rating, recommend, comment, timestamp)
        updateShow(updatedShow)
    }

    fun removeShow(show: Show) {
        deleteShow(show)
    }
}

class SharedViewModelFactory(private val showDao: ShowDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SharedViewModel(showDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}