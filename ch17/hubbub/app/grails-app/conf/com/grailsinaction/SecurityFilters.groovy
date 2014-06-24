package com.grailsinaction

class SecurityFilters {
   def springSecurityService

   def filters = {
      profileChanges(controller: "profile", action: "edit|update") {
         before = {
            def currLoginId = springSecurityService.currentUser.loginId
            if (currLoginId != Profile.get(params.id).user.loginId) {
               redirect controller: "login", action: "denied"
               return false
            }
            return true
         }
      }
   }
}
