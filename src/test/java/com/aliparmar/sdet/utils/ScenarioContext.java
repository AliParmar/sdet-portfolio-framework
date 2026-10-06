package com.aliparmar.sdet.utils;

/** Per-scenario data shared between step-definition classes (injected by PicoContainer). */
public class ScenarioContext {
    private String username;
    private String password;

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
}