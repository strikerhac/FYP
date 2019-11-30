package com.jaemo.jaemo.customer.drawer_fragments.your_rides;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class YourRidesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public YourRidesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is your rides fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}