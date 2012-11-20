package com.grailsinaction

class CreditCardService {

    static scope = "conversation"

    def cardChecked = false
    String cardNumber
    String name
    String expiry
    Date checkedAt

    boolean checkCard(String aNumber, String aName, String anExpiry) {
        (cardNumber, name, expiry) = [ aNumber, aName, anExpiry ]
        log.debug "Validating ${cardNumber} for ${name} with expiry ${expiry}"
        // do remote service here
        checkedAt = new Date()
        cardChecked = true
        println "Card is ${cardChecked}"
        return cardChecked

    }
}
