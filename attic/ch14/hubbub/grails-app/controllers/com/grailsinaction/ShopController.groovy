package com.grailsinaction


class OrderDetailsCommand implements Serializable {

    int numShirts
    int numHats

    boolean isOrderBlank() {
        numShirts == 0 && numHats == 0
    }

    static constraints = {
       numShirts(range: 0..10)
       numHats(range: 0..10)
    }
}

class ShippingCommand implements Serializable {

    String address
    String state
    String postcode
    String country
    boolean customShipping
    String shippingType
    String shippingOptions

}

class PaymentCommand implements Serializable {

    String cardNumber
    String name
    String expiry

    static constraints = {
        cardNumber(creditCard: true)
        name(blank: false, maxSize: 50)
        expiry(matches:"[0-9]{2}/[0-9]{2}")
    }
}



class ShopController {

    def creditCardService

    def index = {
        redirect(action: "order")
    }

    def customShippingFlow = {

        selectShippingType {
            on("next") {
                conversation.sc.shippingType = params.shippingType
            }.to("selectShippingOptions")
            on("standardShipping").to("standardShipping")
            on("previous").to("goBack")
        }

        selectShippingOptions {
            on("previous").to("selectShippingType")
            on("next") {
               conversation.sc.shippingOptions = params.shippingOptions
            }.to("customShipping")
        }

        customShipping()
        standardShipping()
        goBack()
    }

    def orderFlow = {


        displayProducts {
            on("next") { OrderDetailsCommand odc ->
                if (odc.hasErrors() || odc.isOrderBlank()) {
                    flow.orderDetails = odc
                    return error()
                }
                [ orderDetails: odc, orderStartDate: new Date() ]
            }.to("enterAddress")
            on("cancel").to("finish")
        }

        enterAddress {
            on("next") { ShippingCommand sc ->
                conversation.sc = sc
                if (sc.hasErrors()) {
                    return error()
                }
            }.to("checkShipping")
            on("previous").to("displayProducts")
        }

        checkShipping {
            action {
                if (conversation.sc.customShipping) {
                    custom()
                } else {
                    standard()
                }
            }
            on("custom").to("customShipping")
            on("standard").to("enterPayment")
        }

        customShipping {
            subflow(customShippingFlow)
            on("goBack").to("enterAddress")
            on("standardShipping") {
                conversation.sc.customShipping = false
            }.to("enterPayment")
            on("customShipping").to("enterPayment")
        }

        enterPayment {
            on("next") {  PaymentCommand pc ->
                flow.pc = pc
                if (pc.hasErrors()) {
                    return error()
                }
            }.to("validateCard")
            on("previous").to("enterAddress")
        }


        validateCard {
            action {
                //def validCard = new Date().hours > 11 // PM is nice
                def validCard = creditCardService.checkCard(
                    conversation.pc.cardNumber,
                    conversation.pc.name,
                    conversation.pc.expiry)
                if (validCard) {
                    valid()
                } else {
                    flow.pc.errors.rejectValue("cardNumber",
                            "card.failed.validation",
                            "This credit card is dodgy")
                    invalid()
                }
            }
            on("valid").to("orderComplete")
            on("invalid").to("enterPayment")
        }

        orderComplete {
            // display order
            on("finished").to("finish")
        }

        finish {
            redirect(controller:"homePage", action: "index")
        }


    }
}
