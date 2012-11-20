security {
    active = true
    cacheUsers = false

    loginUserDomainClass = "User"
    authorityDomainClass = "Role"
    userName = "userId"
    password = "password"

    // The homepage contains our login form. Note that the default target
    // URL is different to that of the book due to some changes to the
    // application.
    loginFormUrl = "/"
    defaultTargetUrl = "/timeline"

    useRequestMapDomainClass = false

    // Enable HTTP Basic authentication.
    basicProcessingFilter = true

    // "/image/**" and "/post/global" URL rules are new - you won't
    // find them in the book. Down to those application changes as
    // above.
    requestMapString = """\
CONVERT_URL_TO_LOWERCASE_BEFORE_COMPARISON
PATTERN_TYPE_APACHE_ANT
/=IS_AUTHENTICATED_ANONYMOUSLY
/captcha/**=IS_AUTHENTICATED_ANONYMOUSLY
/register/**=IS_AUTHENTICATED_ANONYMOUSLY
/js/**=IS_AUTHENTICATED_ANONYMOUSLY
/css/**=IS_AUTHENTICATED_ANONYMOUSLY
/image/**=IS_AUTHENTICATED_ANONYMOUSLY
/images/**=IS_AUTHENTICATED_ANONYMOUSLY
/plugins/**=IS_AUTHENTICATED_ANONYMOUSLY
/post/global=IS_AUTHENTICATED_ANONYMOUSLY
/user/**=IS_AUTHENTICATED_ANONYMOUSLY
/**=IS_AUTHENTICATED_REMEMBERED
"""
}
