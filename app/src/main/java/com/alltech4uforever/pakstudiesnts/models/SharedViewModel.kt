package com.alltech4uforever.pakstudiesnts.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val correctAns = MutableLiveData<Int>()
    val quesCount = MutableLiveData<Int>()
}