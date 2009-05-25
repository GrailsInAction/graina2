package com.grailsinaction.security

class Sha1Hasher implements Hasher {
    String encode(String str) {
        return str.encodeAsSHA1()
    }
}
