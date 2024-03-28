package com.example.unifolder.source;

import com.example.unifolder.data.user.UserResponseCallback;
import com.example.unifolder.model.User;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;
    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }
    public abstract void getUserRealtime(User user);
    public abstract void saveUserData(User user);
    public abstract void deleteUserRealtime(User user);
    public abstract void setUserAvatar(User user, int selectedImage);

}