package ru.mstoyan.shiko.androidlogin.security;

/**
 * Abstract class to save/load encrypted string.
 */

public abstract class PasswordStorage {
    public abstract void saveLoginData(String name, String password);

    public abstract boolean checkLoginData(String name, String password);

    public abstract boolean isLoginDataSaved();

    public abstract void removeData();
}
