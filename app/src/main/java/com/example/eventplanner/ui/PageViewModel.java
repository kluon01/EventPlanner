package com.example.eventplanner.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    private MutableLiveData<String> mTitle = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mTitle, input -> "In " + input + " Fragment");

    public void setIndex(String index) {
        mTitle.setValue(index);
    }

    public LiveData<String> getText()
    {
        return mText;
    }
}