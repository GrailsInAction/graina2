package com.grailsinaction.security

class AccessControlService {

    static transactional = true

    static scope = "session"

    Long userId

    Account addNewUser(String username, String password) {
        def newUser = new Account(
                username: username,
                passwordHash: hashPassword(password))
        if (newUser.validate()) {
            newUser.save()
        }
        return newUser
    }

    void login(String username, String password) {
        def user = Account.findByUsername(username)
        if (!user) throw new RuntimeException("User $username not found")

        if (user.passwordHash == hashPassword(password)) {
            userId = user.id
        }
        else throw new RuntimeException("Invalid password")
    }

    void logout() { this.userId = null }
    boolean isAuthenticated() { return userId != null }
    String hashPassword(String password) { password.encodeAsMD5() }
}
