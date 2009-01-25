security {
    active = true
    cacheUsers = false

    loginUserDomainClass = "User"
    userName = "userId"
    password = "password"

    useRequestMapDomainClass = false

    requestMapString = """\
CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
PATTERN_TYPE_APACHE_ANT
/=IS_AUTHENTICATED_ANONYMOUSLY
/**=IS_AUTHENTICATED_FULLY
"""
}
