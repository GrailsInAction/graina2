// Place your Spring DSL code here
beans = {
    // This is required for the HTTP Basic authentication to work properly.
    // It fixes the plugin so that a 401 status is sent back to the client
    // for an unauthenticated user, rather than the default 302.
    authenticationEntryPoint(org.springframework.security.ui.basicauth.BasicProcessingFilterEntryPoint) {
        realmName = 'Hubbub'
    }
}
