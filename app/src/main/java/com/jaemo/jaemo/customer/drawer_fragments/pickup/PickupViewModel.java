package com.jaemo.jaemo.customer.drawer_fragments.pickup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PickupViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PickupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is pickup fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}