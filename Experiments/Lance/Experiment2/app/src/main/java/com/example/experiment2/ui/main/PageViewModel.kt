package com.example.experiment2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.experiment2.R

class PageViewModel : ViewModel() {

    private val TAB_TOPICS = arrayOf(
        R.string.topic_one,
        R.string.topic_two,
        R.string.topic_three
    )

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = Transformations.map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }
}