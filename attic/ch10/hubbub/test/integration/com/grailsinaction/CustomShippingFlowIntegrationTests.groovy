package com.grailsinaction

import grails.test.*

class CustomShippingFlowIntegrationTests extends WebFlowTestCase {

    ShopController shopController = new ShopController()

    def getFlow() { shopController.customShippingFlow }

    void testFlowTermination() {

        startFlow()
        getConversationScope().sc = new ShippingCommand()
        shopController.params.shippingType = "express"
        signalEvent("next")
        shopController.params.shippingOptions = "same-day"
        signalEvent("next")
        assertFlowExecutionEnded()
        assertFlowExecutionOutcomeEquals("customShipping")

    }
}
