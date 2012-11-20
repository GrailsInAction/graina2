package com.grailsinaction

import grails.test.*

class OrderFlowIntegrationTests extends WebFlowTestCase {

    ShopController shopController = new ShopController()

    def getFlow() { shopController.orderFlow }

    void testBasicOrder() {

        startFlow()
        assertCurrentStateEquals "displayProducts"

        shopController.params.numShirts = 1
        shopController.params.numHats = 1000
        signalEvent('next')

        assertTrue getFlowScope().orderDetails.hasErrors()
        assertCurrentStateEquals "displayProducts"

        shopController.params.numHats = 1
        signalEvent('next')
        assertCurrentStateEquals "enterAddress"

    }

    void testSubflowTrigger() {

        setCurrentState('enterAddress')

        shopController.params.customShipping = false
        signalEvent('next')
        assertEquals false, getConversationScope().sc.customShipping
        assertCurrentStateEquals "enterPayment"

        signalEvent('previous')
        assertCurrentStateEquals "enterAddress"

        shopController.params.customShipping = true
        signalEvent('next')
        assertEquals true, getConversationScope().sc.customShipping
        assertCurrentStateEquals "selectShippingType"

    }

    void testFlowTermination() {
        
    }


}
