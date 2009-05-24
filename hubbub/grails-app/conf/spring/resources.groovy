// Place your Spring DSL code here
beans = {
    authenticationEntryPoint(org.springframework.security.ui.basicauth.BasicProcessingFilterEntryPoint) {
        realmName = 'Hubbub'
    }
}
