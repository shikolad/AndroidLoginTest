package ru.mstoyan.shiko.androidlogin.security;

/**
 * Abstract class to save/load encrypted string.
 */

public interface PasswordStorage {
    abstract void saveLoginData(String name, String password);

    abstract boolean checkLoginData(String name, String password);

    abstract boolean isLoginDataSaved();

    abstract void removeData();
}
