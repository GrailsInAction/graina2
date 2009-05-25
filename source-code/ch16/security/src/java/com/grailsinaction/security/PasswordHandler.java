package com.grailsinaction.security;

public interface PasswordHandler {
    String encode(String plain);
    boolean passwordsMatch(String plain, String encoded);
}
