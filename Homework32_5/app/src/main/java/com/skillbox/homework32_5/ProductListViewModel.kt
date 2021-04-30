package com.skillbox.homework32_5

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductListViewModel: ViewModel() {
    private val repository = Repository()
    private val _products = MutableLiveData<List<Product>>()
    val products:LiveData<List<Product>> = _products

    fun getProducts() = _products.postValue(repository.getProducts())
}