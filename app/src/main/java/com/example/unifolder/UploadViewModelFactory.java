package com.example.unifolder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.unifolder.Data.User.IUserRepository;
import com.example.unifolder.Welcome.UserViewModel;

public class UploadViewModelFactory implements ViewModelProvider.Factory {
    //private final DocumentRepository documentRepository;
    private final Context context;

    public UploadViewModelFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UploadViewModel.class)) {
            return (T) new UploadViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
