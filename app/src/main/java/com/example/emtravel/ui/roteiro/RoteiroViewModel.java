package com.example.emtravel.ui.roteiro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RoteiroViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RoteiroViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}