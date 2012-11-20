package com.grailsinaction.security

class HashPasswordHandler implements PasswordHandler {
    Hasher hasher
    int iterationCount

    String encode(String plain) {
        return hasher.encode(plain)
    }

    boolean passwordsMatch(String plain, String encoded) {
        def hashedPlain = hasher.encode(plain)
        return hashedPlain == encoded
    }
}
